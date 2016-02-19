# About jGAF

Generic Swing Application Framework (jGAF) is a library designed to facilitate the creation 
of cross-platorm graphical applications for the Oracle Java Platform. The idea of that library 
comes from the needs to have easy-to-use high-level components to create full-featured, multi-OS 
and cross-platform Java applications. Of course, Java Swing provides all the foundations to 
create graphical softwares. However, the writing of a complete UI-based software requires 
large amount of code to setup frames, dialogue boxes, menus, persistent preferences, OS 
integration, etc.

Using jGAF, you can easily and quickly creates cross-platform UI-based Java-Swing applications. 
The library comes with lots of features to save you time and concentrate your effort on the business 
logic of your software.

jGAF is provided with a free-to-use license for free and commercial softwares.

More: [www.plealog.com](www.plealog.com)

# Tutorial

You can have a look at [this project]() to see how to use the library.

# Documentation

Follow [this link](http://www.plealog.com/s/index.php/documents) to read more about jGAF use.

# Java code compiler

Source code of jGAF can be compiled using javac 1.6+.

# Using with an IDE

This project is fully maintained using Eclipse. So it can be easily imported into that IDE. Nevertheless,
I suppose it can be used with any other Java IDEs.

# Using Ant

A build.xml file is provided. It enables to compile and package the jGAF library. 

To create the jgaf.jar file:

     ant package
     
To create JavaDoc:

     ant jdoc

Note: most of the JavaDoc has been written using releases of Java that enabled the lack of
some tags that now seem required by recent doclets (e.g. Java 8+)... so do not worry about 
the many warnings (such as "missing @param")... and I do not have much time to fix that.

# License

jGAF (all code but third party libraries, see below) is released under Apache v2 license.

# Third party libraries

jGAF has been designed to be a unique and self-contained library. For that reason, some external 
codes have been directly included:

* "Base64 encoder/decoder" by Christian d'Heureuse; released under the terms of several open-source licenses; see
com.plealog.genericapp.protection.distrib.HStringCoder.java

* "OSX Adapter" from Apple Inc; released under Apple's open-source license, see 
com.plealog.genericapp.ui.apple.OSXAdapter.java

* "Design Grid Layout" library by Jason Aaron Osgood and Jean-Francois Poilpret; released under Apache v2 license; see
com.plealog.prefs4j.implem.ui.tools package


