# About jGAF - Generic Swing Application Framework

[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html) [![](https://tokei.rs/b1/github/pgdurand/jGAF?category=code)](https://github.com/pgdurand/jGAF) [![](https://img.shields.io/badge/platform-Java--1.8+-yellow.svg)](http://www.oracle.com/technetwork/java/javase/downloads/index.html) [![](https://img.shields.io/badge/run_on-Linux--Mac_OSX--Windows-yellowgreen.svg)]()

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

jGAF provides a framework with:

* [UI Starter API](http://www.plealog.com/s/index.php/features/ui-starter-api): UI Starter API provides the features to setup and start a graphical application with a few lines of code. The API takes into account the application life-cycle and contains pre-configured supports for starting and ending your software.
* [Menu API](http://www.plealog.com/s/index.php/features/menu-api): Menu API is designed to setup menus only from a configuration file. Handling actions within your application only requires to use a single action listener. 
* [Preferences API](http://www.plealog.com/s/index.php/features/preferences-api): Preferences API is a framework to setup a complete multi-pages dialogue box controlling application's configuration. This dialogue box is created using only a set of configuration files, no code is required. Using that API it is even possible to control multi-users configuration and default settings. 
* [OS Integration API](http://www.plealog.com/s/index.php/features/os-integration-api):  OS integration API takes into account the main UI properties of Linux, Windows and MacOS platforms. For instance, considering the MacOS, our API automatically setup your application to conform to that platform: single menu bar, full-screen capability and pre-configuring About, Preferences and Help commands.
* [Persistence API](http://www.plealog.com/s/index.php/features/persistence-api): The first role of Persistence API is to save UI properties of your application, such as last size and screen position. Of course many additional properties can be saved depending on your needs: saving and restoring a property are done with a single line of code. The persistence storage being user-specific, your application will work fine in a multi-user environment. 
* [Utilities API](http://www.plealog.com/s/index.php/features/utilities-api): Utilities API contains additional components to master splash screen, as well as standard messaging, warning, error and input dialogue boxes, to access a logging framework, to manage application resources (e.g. images and icons) and standard FileChooser dialogue boxes for open and save operations. 
* [Protection API](http://www.plealog.com/s/index.php/features/protection-api): if needed, you can protect your application using user name/license key pair protection, library sealed with an expiration date and string obfuscation mechanism.
* [Wizard API](http://www.plealog.com/s/index.php/features/wizard-api): Wizard API contains a convenient model-view system to easily setup dialog box wizards.

jGAF is provided with a free-to-use license for free and commercial softwares (see below).

More: [www.plealog.com](www.plealog.com)

# Tutorial

You can have a look at [this project](https://github.com/pgdurand/jGAF-Tutorial) to see how to use the library.

# Documentation

Follow [this link](http://www.plealog.com/s/index.php/documents) to read more about jGAF use.

# Java code compiler

Source code of jGAF can be compiled using javac 1.8+. We do not support anymore older JDK, since Oracle [dropped down](http://www.webupd8.org/2017/06/why-oracle-java-7-and-6-installers-no.html) the support of Java releases previous to 8.

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

jGAF (all code but third party libraries, see below) is released under [Apache v2 license](https://www.apache.org/licenses/LICENSE-2.0).

# Third party libraries

jGAF has been designed to be a unique and self-contained library. For that reason, some external 
codes have been directly included:

* "Base64 encoder/decoder" by Christian d'Heureuse; released under the terms of several open-source licenses; see
com.plealog.genericapp.protection.distrib.HStringCoder.java

* "OSX Adapter" from Apple Inc; released under Apple's open-source license, see 
com.plealog.genericapp.ui.apple.OSXAdapter.java

* "Design Grid Layout" library by Jason Aaron Osgood and Jean-Francois Poilpret; released under Apache v2 license; see
com.plealog.prefs4j.implem.ui.tools package


