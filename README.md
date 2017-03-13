# CustomRatingBar

Allows you customize a rating bar with your own empty and full icons and sizes.

# Gradle:

compile 'com.kabuki5:logical_rating_bar:1.0'

# Maven:

```xml
<dependency>
  <groupId>com.kabuki5</groupId>
  <artifactId>logical_rating_bar</artifactId>
  <version>1.0</version>
  <type>pom</type>
</dependency>
```

# Usage
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
```
