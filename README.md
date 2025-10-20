# Lego Part Inventory Manager

## Project Overview
This application helps Lego enthusiasts track their collection of **Lego pieces**. Users can enter their inventory of pieces, specifying the types and quantities of pieces they own, such as *bricks*, *plates*, and other *elements* and their size such as 2x4 bricks or 1x4 bricks, etc. The application also allows users to input a Lego build they want to create, checking if they have enough pieces or how many are required to complete the build from their collection. The application supports **saving and loading** the inventory and builds, ensuring users can resume their tracking without losing data. There will also be a graphical user interface where a user can create a new user profiles and view a visual list of all pieces to scroll through. Projects a user wants to work on will show a window with all stats of pieces needed to finish the build or whether the user has enough pieces already.

## Target Users
- **Lego enthusiasts and collectors** who want to track their parts.
- **Builders who create custom models** and need an organized inventory.
- **Hobbyists who plan large builds** and need to know what pieces they lack.

## Why This Project?
I chose this project because I enjoy both Lego and software development. I have been an avid builder from childhood even visiting Legoland and being crowned *Masterbuilder* in building competitions. To combine my passions and create a useful tool I would actually use, this project will provide a program for builders like me who want to optimize their collection and avoid unnecessary purchases. Additionally, designing an application with inventory tracking and database management will be a great learning experience in Java.

## **Class Design**

### **X Class: `LegoPiece` (Represents a Lego part)**
Each Lego piece has:
- **Part Type:** (e.g., "Brick", "Slope", "Plate")
- **Color:** (e.g., "Red", "Blue", "Transparent")
- **Dimensions:** (Width x Height in *stud* units [ex. 2x4, 1x2, 4x4])
- **Quantity:** The number of these parts in user's inventory.


### **Y Class: `LegoInventory` (Represents a Lego part list for a given user)**
Each LegoInventory has:
- **Collection Name:** (e.g., "Shaurya's Collection")
- **Number of Pieces:** (e.g., "1052 pieces")

## **User Stories**

### **As a user, I want to be able to store multiple LegoPiece in an inventory list (LegoInventory)**
- This was implemented through the `LegoInventory` class which stores a list of `LegoPiece`.
- Users can add pieces and duplicates will be added in quantity not as a new piece.
- In the LegoTrackerGUI a user can see all the pieces stores in each inventory.

### **As a user, I want to be able to store all the lists of Lego pieces under my username**
- The `MasterBuilder` class stores a user's name and the multiple lists (`LegoInventory`) of Lego pieces they have in their collection.
- A `Masterbuilder` (user) can also check if a build schematic is buildable with their various lists of Lego pieces.
- In the LegoTrackerGUI a user can see all the pieces stores in each inventory.

### **As a user, I want to be able to create and store Build schematics for things I want to make**
- The `Build` class stores an ID number for a build along with a list of pieces needed to build it.
- The `LegoTrackerGUI` has methods such as `addBuild` and `addPiecesToBuild` that allow the user to update a build's requirements.
- In the LegoTrackerGUI a user can see all the builds they have added to their App and if they are buildable or not with the current pieces they have.
- The list of builds can be called to check whether each build is **buildable** via the `isBuildable` method in the `MasterBuilder` class.

### **As a user, I want to be able build a Build and have my inventory automatically updateds**
- The `LegoTrackerGUI` class contains the `attemptBuild` method which removes all pieces used from a user's inventory if the specified build is buildable and the build has been built.

### **As a user, when I select the quit option from the application menu, I want to be reminded to save my Lego Inventories and list of Builds to file and have the option to do so or not.**

- In the User menu of the program, option to save user profile with all its **Builds** and **LegoInventories** is done through `saveLegoTracker()` method making use of `JsonWriter` class.

### **As a user, when I start the application, I want to be able to load my Lego Inventories and list of Builds from file (if I so choose)**

- In the User menu of the program, option to load user profile with all its **Builds** and **LegoInventories** is done through `loadLegoTracker()` method making use of `JsonReader` class.

# Instructions for End User

- You can generate the first required action related to the user story "adding multiple `LegoPiece` to an `LegoInventory`" by pressing the `Add Pieces to Inventory` button and entering the size, color, dimension, and quantity of pieces you wish to add to an inventory after you have created an inventory for the user by pressing the `Add Inventory` button.
- You can generate the second required action related to the user story "removing multiple `LegoPiece` to an `LegoInventory`" by pressing the `Remove Pieces from Inventory` button and entering the size, color, dimension, and quantity of pieces you wish to remove. If the pieces you remove makes the piece in the inventory go to 0 or less quantity, the piece itself is removed from the inventory.
- You can generate the third required action related to the user story "adding multiple `LegoPiece` to an `Build`" by pressing the `Add Pieces to Build` button after you have added a build to the user by pressing the `Add Build` button. 
- You can locate my visual component when you load up the application: The splash screen with a progress bar.
- You can save the state of my application by pressing the 8th button `Save` to save all data for the user to a local Json file.
- You can reload the state of my application by pressing the 9th button `Load` to load all data previously stored from a local Json file.

# Phase 4: Task 2

Fri Mar 28 20:30:53 PDT 2025
Added 107x Red Brick (2x4) to inventory Shaurya's Inventory

Fri Mar 28 20:30:53 PDT 2025
Added 32x Green Brick (2x8) to inventory Shaurya's Inventory

Fri Mar 28 20:30:53 PDT 2025
Added inventory: Shaurya's Inventory to Shaurya's list of inventories.

Fri Mar 28 20:30:53 PDT 2025
Added 1413x Green Plate (2x6) to inventory Pete's Inventory

Fri Mar 28 20:30:53 PDT 2025
Added 51x Green plate (2x4) to inventory Pete's Inventory

Fri Mar 28 20:30:53 PDT 2025
Added inventory: Pete's Inventory to Shaurya's list of inventories.

Fri Mar 28 20:30:53 PDT 2025
Added 12x Red Brick (2x4) to build 1011

Fri Mar 28 20:30:53 PDT 2025
Added build: 1011 to Shaurya's list of builds.

Fri Mar 28 20:30:53 PDT 2025
Added 5x Red Brick (2x4) to build 101

Fri Mar 28 20:30:53 PDT 2025
Added build: 101 to Shaurya's list of builds.

Fri Mar 28 20:30:53 PDT 2025
Added 200x 2x4 Brick (2x4) to build 1058

Fri Mar 28 20:30:53 PDT 2025
Added build: 1058 to Shaurya's list of builds.

# Phase 4: Task 3

If I had more time to work on this project, one of the key areas I would refactor is the tight coupling between the `MasterBuilder`, `LegoInventory`, and `Build` classes. Currently, the `MasterBuilder` class directly manages collections of both inventories and builds, and also contains logic for checking buildability through the isBuildable method. This design choice violates the Single Responsibility Principle we discussed in class. A better approach would be put the build logic into a dedicated BuildManager class responsible for evaluating builds and managing inventory usage. This separation of concerns would make the codebase more modular, testable, and easier to maintain.

Another improvement would be to reduce the duplication of logic between the text-based UI (LegoTrackerApp) and the GUI (LegoTrackerGUI). Although I used `LegoTrackerApp` class to test my code initially, both classes share similar functionality for adding builds, managing inventories, and handling persistence. Refactoring these shared behaviors into a central class or getting rid of `LegoTrackerApp` would simplify the project. Additionally, introducing interfaces or abstract classes for persistence (e.g. StorageHandler) could allow for better testing and support for alternative storage solutions beyond JSON.