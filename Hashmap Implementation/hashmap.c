#include "hashmap.h"

/**
* initializes num buckets in the hashmap .
* @param *map the hashmap.
* @param *num num of buckets to initialize.
* @return 1 if successful 0 if fail.
*/
int init_buckets (hashmap *map, size_t num)
{
  if (map == NULL || num < 1)
    {
      return 0;
    }
  if (map->buckets != NULL)  // if decreasing capacity
    {
      // frees vectors of buckets that will be freed
      for (size_t i = 0; i < map->capacity; i++)
        {
          vector **vec = &map->buckets[i];
          vector_free (vec);
        }
    }
  // updates buckets
  map->buckets = realloc (map->buckets, sizeof (vector *) * num);
  for (size_t i = 0; i < num; ++i)  // initializes buckets to vectors
    {
      map->buckets[i] = vector_alloc (pair_copy, pair_cmp, pair_free);
    }
  return 1;
}

size_t get_hash (hashmap *map, const_keyT key)
{
  size_t num_key = map->hash_func (key);
  size_t hash_key = num_key & (map->capacity - 1);
  return hash_key;
}

/**
* updates the hashmap capacity.
* @param *map the hashmap.
* @param scale 1 if increase -1 if decrease.
* @return 1 if successful 0 if fail.
*/

int update_capacity (hashmap *hash_map, int scale)
{
  double factor;  // what to multiply capacity by
  if (scale == 1)
    {
      factor = HASH_MAP_GROWTH_FACTOR;
    }
  else
    {
      factor = (double) 1 / HASH_MAP_GROWTH_FACTOR;
    }
  pair *p = NULL;  // temp pair
  //  saves all pairs in hashmap
  pair **saved_pairs = malloc (sizeof (pair) * hash_map->size);
  if (saved_pairs == NULL)
    {
      return 0;
    }
  int pair_ind = 0;  // pair indices
  // saves all pairs in hashmap
  for (size_t i = 0; i < hash_map->capacity; ++i)
    {
      for (size_t j = 0; j < hash_map->buckets[i]->size; ++j)
        {
          p = hash_map->buckets[i]->data[j];
          saved_pairs[pair_ind] = pair_copy (p);
          pair_ind++;
        }
    }
  // clears hashmap
  for (size_t k = 0; k < hash_map->capacity; ++k)
    {
      vector_clear (hash_map->buckets[k]);
    }
  hash_map->size = 0; // updates hashmap size
  // updates buckets and capacity
  init_buckets (hash_map, hash_map->capacity * factor);
  hash_map->capacity = hash_map->capacity * factor;
  // hashes saved pairs with updated hash
  for (int m = 0; m < pair_ind; ++m)
    {
      size_t new_key_hash = get_hash (hash_map, saved_pairs[m]->key);
      vector_push_back (hash_map->buckets[new_key_hash], saved_pairs[m]);
      hash_map->size++;
    }
  // frees saved pairs
  for (size_t h = 0; h < hash_map->size; h++)
    {
      pair **pv = &(saved_pairs[h]);
      pair_free ((void **) pv);
    }
  free (saved_pairs);
  return 1;
}

/**
 * Allocates dynamically new hash map element.
 * @param func a function which "hashes" keys.
 * @return pointer to dynamically allocated hashmap.
 * @if_fail return NULL.
 */
hashmap *hashmap_alloc (hash_func func)
{
  if (func == NULL)
    {
      return NULL;
    }
  hashmap *new = malloc (sizeof (hashmap));
  if (new == NULL)
    {
      return NULL;
    }
  new->capacity = HASH_MAP_INITIAL_CAP;
  new->buckets = NULL;
  init_buckets (new, HASH_MAP_INITIAL_CAP);
  new->size = 0;
  new->hash_func = func;
  return new;
}

/**
 * Frees a hash map and the elements the hash map itself allocated.
 * @param p_hash_map pointer to dynamically allocated pointer to hash_map.
 */
void hashmap_free (hashmap **p_hash_map)
{
  if (p_hash_map != NULL)
    {
      // frees vectors of buckets
      for (size_t i = 0; i < (*p_hash_map)->capacity; i++)
        {
          vector **vec = &(*p_hash_map)->buckets[i];
          vector_free (vec);
        }
      free ((*p_hash_map)->buckets);  // frees buckets
      free (*p_hash_map);  // frees hashmap
      (*p_hash_map) = NULL;
      p_hash_map = NULL;
    }
}

/**
 * Inserts a new in_pair to the hash map.
 * The function inserts *new*, *copied*, *dynamically allocated* in_pair,
 * NOT the in_pair it receives as a parameter.
 * @param hash_map the hash map to be inserted with new element.
 * @param in_pair a in_pair the hash map would contain.
 * @return returns 1 for successful insertion, 0 otherwise.
 */
int hashmap_insert (hashmap *hash_map, const pair *in_pair)
{
  if (hash_map == NULL || in_pair == NULL)
    {
      return 0;
    }
  if (hashmap_at (hash_map, in_pair->key))  // if key already in hashmap
    {
      return 0;
    }
  size_t key_hash = get_hash (hash_map, in_pair->key);  // hashed key
  vector_push_back (hash_map->buckets[key_hash], in_pair); // inserts
  hash_map->size++;  // updates size
  // if load factor too big updates capacity and rehashes
  if (hashmap_get_load_factor (hash_map) > HASH_MAP_MAX_LOAD_FACTOR)
    {
      if (!update_capacity (hash_map, 1))
        {
          return 0;
        }
    }
  return 1;
}

/**
 * The function returns the value associated with the given key.
 * @param hash_map a hash map.
 * @param key the key to be checked.
 * @return the value associated with key if exists, NULL otherwise
 * (the value itself,
 * not a copy of it).
 */
valueT hashmap_at (const hashmap *hash_map, const_keyT key)
{
  if (hash_map == NULL || key == NULL)
    {
      return NULL;
    }
  size_t num_key = hash_map->hash_func (key);
  size_t hash_key = num_key & (hash_map->capacity - 1);
  pair *value = NULL;  // gets value
  size_t i;  // indices for data
  // finds the element with same key and returns the value
  for (i = 0; i < hash_map->buckets[hash_key]->size; i++)
    {
      value = hash_map->buckets[hash_key]->data[i];
      if (value->key_cmp (key, value->key))
        {
          return value->value;
        }
    }
  // if key not found
  if (i >= hash_map->buckets[hash_key]->size)
    {
      return NULL;
    }
  return NULL;
}

/**
 * The function erases the pair associated with key.
 * @param hash_map a hash map.
 * @param key a key of the pair to be erased.
 * @return 1 if the erasing was done successfully, 0 otherwise.
 * (if key not in map, considered fail).
 */
int hashmap_erase (hashmap *hash_map, const_keyT key)
{
  if (hash_map == NULL || key == NULL)
    {
      return 0;
    }
  size_t hash_key = get_hash (hash_map, key);
  // if bucket is empty
  if (hash_map->buckets[hash_key]->size <= 0)
    {
      return 0;
    }
  int ind = -1;  // if not found -1 if found = index
  pair *p = NULL;  // temp pair
  //  makes sure key is in hashmap
  for (size_t j = 0; j < hash_map->buckets[hash_key]->size; ++j)
    {
      p = hash_map->buckets[hash_key]->data[j];
      if (p->key_cmp (p->key, key))
        {
          ind = j;
          break;
        }
    }
  if (ind == -1)  // if key not found
    {
      return 0;
    }
  // erases element
  vector_erase (hash_map->buckets[hash_key], ind);
  hash_map->size--;
  // if load factor too small updates capacity and rehashes hashmap
  if (hashmap_get_load_factor (hash_map) < HASH_MAP_MIN_LOAD_FACTOR)
    {
      update_capacity (hash_map, -1);
    }
  return 1;
}

/**
 * This function returns the load factor of the hash map.
 * @param hash_map a hash map.
 * @return the hash map's load factor, -1 if the function failed.
 */
double hashmap_get_load_factor (const hashmap *hash_map)
{
  if (hash_map == NULL)
    {
      return -1;
    }
  return (double) hash_map->size / hash_map->capacity;
}

/**
 * This function receives a hashmap and 2 functions, the first checks a
 * condition on the keys, and the seconds apply some modification on the
 * values. The function should apply the modification
 * only on the values that are associated with keys that meet the condition.
 *
 * Example: if the hashmap maps char->int, keyT_func checks if the char is a
 * capital letter (A-Z),cand val_t_func multiples the number by 2,
 * hashmap_apply_if will change the map: {('C',2),('#',3),('X',5)},
 * to: {('C',4),('#',3),('X',10)}, and the return value will be 2.
 * @param hash_map a hashmap
 * @param keyT_func a function that checks a condition on keyT and return 1 if
 * true, 0 else
 * @param valT_func a function that modifies valueT, in-place
 * @return number of changed values
 */
int hashmap_apply_if (const hashmap *hash_map,
                      keyT_func keyT_func, valueT_func valT_func)//const
{
  if (hash_map == NULL || keyT_func == NULL || valT_func == NULL)
    {
      return -1;
    }
  pair *p = NULL; // gets pairs
  int counter = 0;  // amount updated
  // checks every pair in map
  for (size_t i = 0; i < hash_map->capacity; i++)
    {
      for (size_t j = 0; j < hash_map->buckets[i]->size; j++)
        {
          p = hash_map->buckets[i]->data[j];
          if (keyT_func (p->key))  // if fulfills keyT_func
            {
              valT_func (p->value);  // updates value
              counter++;
            }
        }
    }
  return counter;
}

