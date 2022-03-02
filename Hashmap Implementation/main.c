#include <stdio.h>
#include "vector.h"
#include "pair.h"
#include "hashmap.h"
#include "hash_funcs.h"
#include "pair_char_int.h"

void *elem_cpy (const void *elem){
  int *a = malloc (sizeof (int));
  *a = *((int *) elem);
  return a;
}
int elem_cmp (const void *elem_1, const void *elem_2){
  return *((const int *) elem_1) == *((const int *) elem_2);
}
void elem_free (void **elem){
  free (*elem);
}

#define PAIRS_NUM 3

int main (){
  pair *pairs[PAIRS_NUM];
  for (int i = 0; i<PAIRS_NUM; i++)
    {
      char key =(char) (i + 48);
      if (key%2)
        {
          key +=17;
        }
      int val = i;
      pairs[i] = pair_alloc(&key, &val, char_key_cpy, int_value_cpy,
                            char_key_cmp, int_value_cmp, char_key_free,
                            int_value_free);
    }
  for(int k = 0; k<PAIRS_NUM; k++)
    {
      printf("%c:%d\n", *((char*)pairs[k]->key), *((int*)pairs[k]->value));
    }

  hashmap *map = hashmap_alloc(hash_char);
  for(int j=0;j<PAIRS_NUM;j++)
    {
      hashmap_insert(map, pairs[j]);
      printf("cap:%zu, ", map->capacity);
    }
  printf("\n");
  for (int l = 0; l< PAIRS_NUM;l++)
    {
      char x = *(char*)pairs[l]->key;
      printf("%c:%d, ",x, *(int*)hashmap_at(map, pairs[l]->key));
    }
  printf("\n");
  hashmap_erase(map, pairs[1]->key);
  pair *tmp = NULL;
  for (int p = 0; p < map->capacity;p++)
    {
      for(int h = 0; h<map->buckets[p]->size; h++)
        {
          tmp = map->buckets[p]->data[h];
          printf("%c:%d, ", *(char*)tmp->key, *(int*)tmp->value);
        }
    }
  printf("\ncap:%zu\n", map->capacity);
  for(int h = 0; h< PAIRS_NUM; h++)
    {
      pair **pv = &(pairs[h]);
      pair_free((void**)pv);
    }
  hashmap_free(&map);
  printf("END\n");

  /*// Insert elements to vec.
  vector *vec = vector_alloc (elem_cpy, elem_cmp, elem_free);
  for (int i = 0; i < 8; ++i){
      vector_push_back (vec, &i);
    }
  vector_free (&vec);
  // Create Pairs.
  pair *pairs[8];
  for (int j = 0; j < 8; ++j){
      char key = (char) (j + 48);
      //even keys are capital letters, odd keys are digits
      if (key % 2){
          key += 17;
        }
      int value = j;
      pairs[j] = pair_alloc (&key, &value, char_key_cpy, int_value_cpy, char_key_cmp,int_value_cmp, char_key_free, int_value_free);
    }// Create hash-map and inserts elements into it, using pair_char_int.h
  hashmap *map= hashmap_alloc (hash_char);
  for (int k = 0; k < 8; ++k){
      hashmap_insert (map, pairs[k]);
    }
  //apply double_value on values where key is a digit
  char key_dig = '2', key_letter = 'D';
  printf ("map['2'] before apply if: %d, map['D'] before apply if: %d\n", *(int *) hashmap_at (map, &key_dig), *(int *) hashmap_at (map, &key_letter));
  int apply_if_res = hashmap_apply_if (map, is_digit, double_value);
  printf ("Number of changed values: %d\n", apply_if_res);
  printf ("map['2'] after apply if: %d, map['D'] after apply if: %d\n",
          *(int *) hashmap_at (map, &key_dig), *(int *) hashmap_at (map, &key_letter));
  // Free the pairs.
  for (int k_i = 0; k_i < 8; ++k_i){
      pair_free ((void **) &pairs[k_i]);
    }// Free the hash-map.
  hashmap_free (&map);
  printf("END\n");
  return 0;*/


  /*vector *vec = vector_alloc(elem_cpy, elem_cmp,elem_free);
  for (int k = 1; k<=3; k++)
    {
      vector_push_back(vec, &k);
    }
  printf("cap: %d\n", (int)vec->capacity);
  printf("in:\n");
  for(int i = 0; i< vec->size; i ++)
    {
      printf("%d, ", *((int*)vec->data[i]));
    }
  printf("\nv_at: %d\n", *((int*)vector_at(vec, 0)));
  int x = 3;
  printf("v_find: %d\n", vector_find(vec, &x));
  vector_erase(vec, 2);
  vector_erase(vec, 1);
  int p = 4;
  vector_push_back(vec, &p);
  printf("out:\n");
  for(int i = 0; i< vec->size; i ++)
    {
      printf("%d, ", *((int*)vec->data[i]));
    }
  printf("\n");
  vector_free(&vec);
  printf("END");*/
}

/*// Insert elements to vec.
  vector *vec = vector_alloc (elem_cpy, elem_cmp, elem_free);
  for (int i = 0; i < 8; ++i){
      vector_push_back (vec, &i);
    }
  vector_free (&vec);
  // Create Pairs.
  pair *pairs[8];
  for (int j = 0; j < 8; ++j){
      char key = (char) (j + 48);
      //even keys are capital letters, odd keys are digits
      if (key % 2){
          key += 17;
        }
      int value = j;
      pairs[j] = pair_alloc (&key, &value, char_key_cpy, int_value_cpy, char_key_cmp,int_value_cmp, char_key_free, int_value_free);
    }// Create hash-map and inserts elements into it, using pair_char_int.h
  hashmap *map= hashmap_alloc (hash_char);
  for (int k = 0; k < 8; ++k){
      hashmap_insert (map, pairs[k]);
    }
  //apply double_value on values where key is a digit
  char key_dig = '2', key_letter = 'D';
  int* x = (int *) hashmap_at (map, &key_dig);
  printf ("map['2'] before apply if: %d, map['D'] before apply if: %d\n", *(int *) hashmap_at (map, &key_dig), *(int *) hashmap_at (map, &key_letter));
  int apply_if_res = hashmap_apply_if (map, is_digit, double_value);
  printf ("Number of changed values: %d\n", apply_if_res);
  printf ("map['2'] after apply if: %d, map['D'] after apply if: %d\n",
          *(int *) hashmap_at (map, &key_dig), *(int *) hashmap_at (map, &key_letter));
  // Free the pairs.
  for (int k_i = 0; k_i < 8; ++k_i){
      pair_free ((void **) &pairs[k_i]);
    }// Free the hash-map.
  hashmap_free (&map);
  return 0;*/