# CustomRatingBar

Allows you customize a rating bar with your own empty and full icons and sizes.

Usage: 
```xml

<com.kabuki5.logicalratingbar.CustomRatingBar
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:clickable="true"
        app:iconSize="58dp"
        app:imageEmpty="@drawable/ic_heart_empty"
        app:imageFull="@drawable/ic_heart_full"
        app:initRating="5"
        app:maxItems="5" />
        
