# android-instagram

## Overview

This is the project for the Instagram Client Mockup Project. 


## User Stories
The following **required** functionality is completed:
Assignment 1: 
* User can scroll through the popular posts from Instagram.
* For each post displayed, user can see the following details: 
  1. Graphic, Caption, Username, User profile image
  2. Relative timestamp, Like count
* Display each user profile image as a circle.
* Display a nice default placeholder graphic for each image during loading.

Assignment 2:
* Connect the app with the Instagram API and get real time data using async-http-client library.
* Show the last 2 comments for each photo.
* User can view all comments for a post within a separate activity.
* User can share an image to their friends or email to themselves.

Assigment 3:
* User can login to Instagram using OAuth login.
* User can view their own feed.
* User can search for a user by username.
* User can search for a tag.
  1. On the search screen, there will now be 2 tabs corresponding to "USERS" and "TAGS".
  2. When performing a search, the user can switch between the "USERS" tab and the "TAGS" tab and see search results for each one.

Assigment 4:
* Add pull-to-refresh for the home feed with SwipeRefreshLayout.
* User can open the Instagram client offline and see last loaded feed.
   Each media item is persisted into SQlite and can be displayed from the local DB.
* Create a background service to make the network request, load items into the DB and populate the view (for the user's home feed).

Assigment 5:
* Add the Profile Page to your TabLayout to see your own posts, follower count, following count. 
* Replace the ActionBar with a ToolBar and style it to match the actual Instagram app. 
* Add infinite scroll to your home feed. 


The following **optional** functionality is completed:
* Display each post with the same style and proportions as the real Instagram (design mock up provided below).


## Video Walkthrough 

Assigment 5: 
![Imgur](http://i.imgur.com/lQWsI4C.gif)

Assgiment 4:
![Imgur](http://i.imgur.com/cUBVjMF.gif)

Assigment 3: 
![Imgur](http://i.imgur.com/dxLxOW8.gif)

Assigment 2:
![Imgur](http://i.imgur.com/Z696aiD.gif)

Assigment 1:
![Imgur](http://i.imgur.com/fEeC37e.gif)

## Finished Project
* By the end of the project, you will have built something that looks like the following:

  ![Imgur](http://i.imgur.com/4SWlsQA.gif)
