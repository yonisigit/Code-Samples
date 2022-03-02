//
// Created by Barak Raveh on 27/05/2021.
//
#include "vector.h"
#include <stdio.h>
#include <assert.h>

/**
 * Copies an unsigned int
 */
void* uint_cpy(const void* value)
{
  unsigned int *new_uint = malloc (sizeof (unsigned int));
  *new_uint = *((unsigned int*)value);
  return new_uint;
}


/**
 * Compares unsigned ints
 */
int uint_cmp(const void* value_1, const void* value_2)
{
  return *((unsigned int*)value_1) == *((unsigned int*)value_2);
}

/**
 * Frees an unsigned int
 */
void uint_free(void** p_value)
{
  if (p_value && *p_value)
    {
      free (*p_value);
      *p_value = NULL;
    }
}


// note this assumes vector_alloc() and vector_free() work
// correctly - we may add a different tester for them (note
// you weren't asked to)
void test_vector_push_back()
{
  const unsigned int n = 20; // number of elements to insert, could be a #define if you like
  // Allocate
  vector* v= vector_alloc(&uint_cpy, &uint_cmp, &uint_free);
  assert(v); // in a real setup, return a different value ifout of memory
  // Test numbers are added at correct index
  for(unsigned int i = 0 ; i < n; i++)
    {
      int is_success = vector_push_back(v, &i);
      assert(is_success);
      assert(uint_cmp(v->data[i], &i));
    }
  // Deallocate
  vector_free(&v);
}

// note this assumes push_back, alloc and free work correctly
void test_vector_at()
{
  const unsigned int n = 20; // number of elements to insert, could be a #define if you like
  // Allocate
  vector* v= vector_alloc(&uint_cpy, &uint_cmp, &uint_free);
  assert(v); // in a real setup, return a different value ifout of memory
  // I. test at() returns a correct value:
  for(unsigned int i = 0 ; i < n; i++)
    {
      int is_success = vector_push_back(v, &i);
      assert(is_success);
      unsigned int* at = vector_at(v, i);
      assert(at != NULL);
      assert(uint_cmp(at, &i));
    }
  // II. test at() fails when out of bounds
  unsigned int* at = vector_at(v, n);
  assert(at == NULL);
  // Deallocate
  vector_free(&v);
}

int main()
{
  test_vector_push_back();
  test_vector_at();
}
