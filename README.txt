========
OVERVIEW
========

This was an interesting project, not too difficult, but it certainly offered opportunities for good
design (principles of encapsulation) and unit testing. While the assignment did not call for it
specifically, completeness demanded that I devote a lot of code (and unit tests) to validation.
Handling things like

 - missing fields in a csv line
 - improperly formatted dates
 - delivery records that refer to placements that don't exist
 - date ranges where the end date precedes the start date.

 Similarly, I tried to be as tolerant as possible, noticing that in the sample input files, there was
 some inconsistency with dates (some like 12/1/20 and others 12/15/2020). These little quirks added
 small demands on both the actual classes and the unit tests.

============================
PACKAGING AND OPERATIONS
============================

The source code is delivered in the form of a git bundle file. You can convert that file into a local instance
of my 'placements' git repository with this command.
    git clone placements.bundle
Then, cd into that 'placements' directory where you will be at the 'root' directory that contains all my source code.

Note, also, that you will see input files for the program
 - delivery.csv
 - placements.csv
 - dateQuery.txt

I used maven for both dependencies and a way to build the program. Run
  mvn package
to build the program


