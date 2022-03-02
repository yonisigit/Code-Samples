.PHONY = test

CCFLAGS = -Wall -Wextra -Wvla -Werror -g -lm -std=c99


libhashmap_tests.a: libhashmap.a
	ar rcs $@ $^


libhashmap.a: hashmap.o vector.o pair.o
	ar rcs libhashmap.a hashmap.o vector.o pair.o 


test_suite.o: test_suite.c test_suite.h hashmap.h test_pairs.h hash_funcs.h
	gcc $(CCFLAGS) -c test_suite.c -o test_suite.o


hashmap.o: hashmap.c hashmap.h
	gcc $(CCFLAGS) -c hashmap.c -o hashmap.o


vector.o: vector.c vector.h
	gcc $(CCFLAGS) -c vector.c -o vector.o


pair.o: pair.c pair.h
	gcc $(CCFLAGS) -c pair.c -o pair.o
