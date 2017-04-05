# StringListGen

The ListGenerator class reads the next line from a text file and return a List of Strings that represent each
delimited token that it reads from the file. Determining which parser type to use is based on the extension of the
filename. For example, with files of type “*.tab”, given the following file contents:

This<tab>is<tab>a<tab>test
red<tab>green<tab>blue

The first call to a getNextLineTokens method returns a List of Strings that includes “This”, “is”, “a” and “test”.
The second call returns “red”, “green” and “blue”. A third call will return null.

Created by Travis on 3/14/2017.