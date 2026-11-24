# TUS Equipment Booking (https://github.com/Code-Lau/TUSEquipmentBooking)

This is a mobile app that allows users to browse and book equipment.
It is built entirely in Kotlin and supported by Firebase. A student must log in to book equipment, 
of which they can borrow from 1 to 5 days. Equipment and bookings are stored on the device, while 
Firebase provides the user login. 

## How to run

### Requirements
- Android Studio with JDK 11+
- A physical device or an emulator running Android 7+ with internet access.

1. Unzip the project and open the "TUSEquipmentBooking.zip" folder in Android Studio.
2. Sync Gradle.
3. Check that google-services.json is present (app/google-services.json).
4. Select an appropriate emulator or device.
5. Press run.

### Firebase Authentication
- This app uses Firebase Authentication to log in users, and as such is already connected to a Firebase project.
- If you wish to use your own Firebase project, you will need to:

1. Create a project through the Firebase website and add an android app with the package name: "com.example.tusequipmentbooking".
2. Download the google-services.json file and place it in the app folder.

### Testing
- There are no premade user accounts, so to test you will have to register a new account. Any @gmail.com address and any password will
currently do. It was tested with a real TUS student account and works perfectly.

### Current Limitations
- Currently, the equipment and booking only exist on the device itself, availability isn't shared between devices, if this
were a real app this would need to be expanded upon.
- There's no admin management, no CRUD for category or equipment, it's all hardcoded at the moment for demonstration purposes.
- Bookings can't be made in advance, they start today and don't automatically expire.

### Content & AI
- This project was made entirely based on the labs provided by the module, adjusted and rearranged 
appropriately for the project's needs.
- No major AI generated content was used, however AI was used to help debug and solve issues with certain abnormalities.
Particularly minor errors between lab content and issues with dependencies.