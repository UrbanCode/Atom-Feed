# IBM UrbanCode Deploy - Atom Feed Plug-in[![Build Status](https://travis-ci.org/IBM-UrbanCode/Atom-Feed.svg?branch=master)](https://travis-ci.org/IBM-UrbanCode/Atom-Feed)
---
Note: This is not the plugin distributable! This is the source code. To find the installable plugin, go into the 'Releases' tab, and download a stable version.

### License
This plugin is protected under the [Eclipse Public 1.0 License](http://www.eclipse.org/legal/epl-v10.html)

### Compatibility
	This plug-in requires version 6.1.1 or later of IBM UrbanCode Deploy.

### Installation
	The packaged zip is located in the releases folder. No special steps are required for installation. See Installing plug-ins in UrbanCode Deploy. Download this zip file if you wish to skip the manual build step. Otherwise, download the entire Atom-Feed-Injector-UCD project and run the `gradle` command in the top level folder. This should compile the code and create 	a new distributable zip within the `build/distributions` folder. Use this command if you wish to make your own changes to the plugin.

### Steps
    - Inject Entry: Create and inject new Atom Feed Entry from Atom Feed.

### History
    Version 1
        - Community GitHub Release

### How to build the plugin from command line:

1. Navigate to the base folder of the project through command line.
2. Make sure that there is a build.gradle file in the root directory and execute the 'gradle' command.
3. The built plugin is located at `build/distributions/Atom-Feed-Injector-UCD-vdev.zip`
