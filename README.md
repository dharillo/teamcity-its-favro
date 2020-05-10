# TeamCity server-side plugin

![Java CI with Maven](https://github.com/dharillo/teamcity-its-favro/workflows/Java%20CI%20with%20Maven/badge.svg)

TeamCity plugin to use Favro as Issue Tracker

## How to build

1. Clone the repository and execute ```mvn package```

2. The plugin zip file generated can be found at the root *target* folder

## Installation

1. Go to the administration section of TeamCity

2. In the left side menu, at the bottom, click on Plugin List

3. Click on upload ZIP

4. Upload the generated ZIP

## Configuration

To configure the plugin, go to any project on your TeamCity instance. In the project configuration page, go to the Issue Tracker section. There you will be able to add a new Issue Tracker and select Favro.

To use Favro, you will need to insert your organization ID. This organization ID is visible in the URL of your Favro collections. You will also need to specify a user with its password or an API token.

The plugin detects a task association when the commit message includes the text #ABC-123. The letters may vary depending on your organization. The exact text to indicate is the identifier that you will see on any card of your collections.
