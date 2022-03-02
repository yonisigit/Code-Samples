#include <stdio.h>
#include "test_suite.h"
#include "pair.h"
#include "vector.h"
#include "hashmap.h"
#include "test_pairs.h"
#include "hash_funcs.h"

// tests for char-int pairs

/**
 * This function checks the hashmap_insert function of the hashmap library.
 * If hashmap_insert fails at some points, the functions exits with exit code
 * 1.
 */
void test_hash_map_insert (void)
{
  hashmap *map = hashmap_alloc (hash_char);
  // creates pairs for map
  pair *pairs[13];
  for (int i = 1; i <= 13; i++)
    {
      char key = (char) (96 + i);
      int val = i;
      pairs[i - 1] = pair_alloc (&key, &val, char_key_cpy, int_value_cpy,
                                 char_key_cmp, int_value_cmp, char_key_free,
                                 int_value_free);
    }
  size_t size = map->size;
  // checks that all pairs were added and map updated
  for (int j = 0; j < 13; j++)
    {
      pair *p = pairs[j];
      hashmap_insert (map, p);
      assert(*(int*)p->value == *(int*)(hashmap_at (map, p->key)));
      assert(size == map->size - 1);
      size++;
    }
  int cap = map->capacity;
  assert(cap == 32);  // checks that capacity updated
  size = map->size;
  // checks invalid inputs
  hashmap_insert (map, NULL);
  assert(size == map->size);
  hashmap_insert (NULL, pairs[1]);
  assert(size == map->size);
  pair *test_pair = pair_alloc (pairs[0]->key, pairs[0]->value,
                                char_key_cpy, int_value_cpy, char_key_cmp,
                                int_value_cmp, char_key_free, int_value_free);
  hashmap_insert (NULL, test_pair);
  assert(size == map->size);
  // frees pairs and map
  pair_free((void**)&test_pair);
  for (int k_i = 0; k_i < 13; ++k_i)
    {
      pair_free ((void **) &pairs[k_i]);
    }
  hashmap_free (&map);
  printf("Passed Insert Tests.\n");
}

/**
 * This function checks the hashmap_at function of the hashmap library.
 * If hashmap_at fails at some points, the functions exits with exit code 1.
 */
void test_hash_map_at (void)
{
  hashmap *map = hashmap_alloc (hash_char);
  // checks that elements that are not in map return NULL
  char c = 'a', b = 'b';
  assert(hashmap_at(map, &c) == NULL);
  assert(hashmap_at(map, &b) == NULL);
  // creates pairs for map
  pair *pairs[3];
  for (int i = 1; i <= 3; i++)
    {
      char key = (char) (96 + i);
      int val = i;
      pairs[i - 1] = pair_alloc (&key, &val, char_key_cpy, int_value_cpy,
                                 char_key_cmp, int_value_cmp, char_key_free,
                                 int_value_free);
    }
  // adds pairs and checks that they are found
  for (int j = 0; j < 3; j++)
    {
      pair *p = pairs[j];
      hashmap_insert (map, p);
      assert(*(int*)p->value == *(int*)(hashmap_at (map, p->key)));
    }
  // checks again that returns NULL if not in map
  char p = 'p';
  assert(hashmap_at(map, &p) == NULL);
  // checks invalid input
  assert (hashmap_at(NULL, pairs[0]) == NULL);
  assert(hashmap_at(map, NULL) == NULL);
  // frees
  for (int k_i = 0; k_i < 3; ++k_i)
    {
      pair_free ((void **) &pairs[k_i]);
    }
  hashmap_free (&map);
  printf("Passed at Tests.\n");
}

/**
 * This function checks the hashmap_erase function of the hashmap library.
 * If hashmap_erase fails at some points, the functions exits with exit code
 * 1.
 */
void test_hash_map_erase (void)
{
  hashmap *map = hashmap_alloc (hash_char);
  // creates pairs for map
  pair *pairs[3];
  for (int i = 1; i <= 3; i++)
    {
      char key = (char) (96 + i);
      int val = i;
      pairs[i - 1] = pair_alloc (&key, &val, char_key_cpy, int_value_cpy,
                                 char_key_cmp, int_value_cmp, char_key_free,
                                 int_value_free);
    }
  // inserts pairs
  for (int j = 0; j < 3; j++)
    {
      pair *p = pairs[j];
      hashmap_insert (map, p);
    }
  //  checks that key thats not in map returns 0
  char c = 'i';
  assert(hashmap_erase(map, &c) == 0);
  // invalid input
  assert(hashmap_erase(NULL, pairs[0]) == 0);
  assert(hashmap_erase(map, NULL) == 0);
  // checks that elems were erased and capacity updated
  hashmap_erase(map, pairs[1]->key);
  assert(map->capacity == 8);
  assert (NULL == hashmap_at(map, pairs[1]->key));
  hashmap_erase(map, pairs[0]->key);
  assert (NULL == hashmap_at(map, pairs[0]->key));
  assert(map->capacity == 4);
  // frees
  for (int k_i = 0; k_i < 3; ++k_i)
    {
      pair_free ((void **) &pairs[k_i]);
    }
  hashmap_free (&map);
  printf("Passed Erase Tests.\n");
}
/**
 * This function checks the hashmap_get_load_factor function of the hashmap
 * library.
 * If hashmap_get_load_factor fails at some points, the functions exits with
 * exit code 1.
 */
void test_hash_map_get_load_factor (void)
{
  hashmap *map = hashmap_alloc (hash_char);
  // adds pairs to map
  pair *pairs[3];
  for (int i = 1; i <= 3; i++)
    {
      char key = (char) (96 + i);
      int val = i;
      pairs[i - 1] = pair_alloc (&key, &val, char_key_cpy, int_value_cpy,
                                 char_key_cmp, int_value_cmp, char_key_free,
                                 int_value_free);
    }
  // adds elems and checks load factor
  for (int j = 0; j < 3; j++)
    {
      pair *p = pairs[j];
      hashmap_insert (map, p);
      assert(hashmap_get_load_factor(map) ==
                                      ((double) (j+1)/VECTOR_INITIAL_CAP));
    }
  // invalid input
  assert((hashmap_get_load_factor(NULL)) == -1);
  // frees
  for (int k_i = 0; k_i < 3; ++k_i)
    {
      pair_free ((void **) &pairs[k_i]);
    }
  hashmap_free (&map);
  printf("Passed Load Factor Tests.\n");
}

/**
 * This function checks the HashMapGetApplyIf function of the hashmap library.
 * If HashMapGetApplyIf fails at some points, the functions exits with exit
 * code 1.
 */
void test_hash_map_apply_if ()
{
  // creates pairs
  pair *pairs[8];
  for (int j = 0; j < 8; ++j){
      char key = (char) (j + 48);
      //even keys are capital letters, odd keys are digits
      if (key % 2){
          key += 17;
        }
      int value = j;
      pairs[j] = pair_alloc (&key, &value, char_key_cpy, int_value_cpy,
                             char_key_cmp,int_value_cmp, char_key_free,
                             int_value_free);
    }// Create hash-map and inserts elements into it, using pair_char_int.h
  hashmap *map= hashmap_alloc (hash_char);
  for (int k = 0; k < 8; ++k){
      hashmap_insert (map, pairs[k]);
    }
  int apply_if_res = hashmap_apply_if (map, is_digit, double_value);
  assert(apply_if_res == 4);  // checks that right amount updated
  for (int k_i = 0; k_i < 8; ++k_i){
      pair_free ((void **) &pairs[k_i]);
    }// Free the hash-map.
  hashmap_free (&map);
  printf("Passed Apply If Tests.\n");
}


int main()
{
  test_hash_map_insert();
  test_hash_map_at();
  test_hash_map_get_load_factor();
  test_hash_map_erase();
  test_hash_map_apply_if();
}
