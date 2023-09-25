All changes regarding the project and API will be documented here. The latest version wIll always be on top.
## Build 0.0.153 
Changes here are made when the RenJava project was still private.
- Added [Container]() which supports layouts and additional alignment features.
  - Started implementation of the save functionality. All classes which contain data you want to save must be implemented with `PersistentData`.
  All fields that contain the data you want to save cannot be final and must be annotated with `@Data`.
- Re-structured [Story]() class. Story class use to be abstract however upon testing and feedback it will work the same as scenes. JavaDoc has been updated to reflect these changes.
- The RenJava [Player]() will now automatically track the stories when they start.
- Expanded [StageType]()
- Duration for splash-screens is now configurable.
- Completed buttons for choices in the [ChoiceScene]()
- Expanded documentation.

### TODO / BUGS
- PreferenceScreen still incomplete.
- Tracks not fully implemented.
- Save/Load screens not implemented.
- RPA files not supported (will be the case for the foreseeable future)