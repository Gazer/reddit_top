# Reddit Top Posts Example

Android App to read Top posts from Reedit.com

---
>Made with 💙 by Ricardo Markiewicz // [@gazeria](https://twitter.com/gazeria).

## Features

- Pull to Refresh
- Pagination support
- Saving pictures in the picture gallery
- App state-preservation/restoration when the screen rotate
- Indicator of unread/read post (updated status, after post it’s selected)
- Dismiss Post Button (remove the cell from list. Animations required)
- Dismiss All Button (remove all posts. Animations required)
- Support split layout (left side: all posts / right side: detail post)

## Dependencies

* 100% Kotlin
* MVVM Design Pattern
* Room for Database
* Retrofit for HTTP request
* Paging3 (beta, but seems stable) for infinite list loading
* ViewModel, LiveData and stuff
* Coroutines for background work
* Picasso for image loading
* Hilt for Dependecy Injection

## Design Choises

Paging3 does not support to remove an element easily. That is the reason I introduce a local database to save the entries and use a PaginSource from room instead of create a custom Repository.

This may not the best option for this app that does not require offline support, but not pretty well.

## TODOs

The app is not perfect and can be improved:

* Move strings from Layout to string.xml for localiztion
* Image saving is a dirty hack, with time could be improved
* There is a bug that if a fetch is canceled when the list is being created for first time the list do not start scrolled at the top.
* Add tests :)
