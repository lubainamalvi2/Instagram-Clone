Project: Instagram Clone

Group members: Lubaina Malvi, Levin Sanchez, Armaan Mehra, and Jahan Goel

Developed on Pixel 9 Pro API 35

When creating an account please use a valid email address
when testing forgot password, the email recieved will be from lmalvi2000@gmail.com - make sure to check your junk mail
If the email is taking too long to send, you can uncomment the log inside of ForgotPasswordViewModel to see the code that was generated in the backend and saved to the database.
Changes made to Gradle file:

For Glide: 
implementation("com.github.bumptech.glide:glide:4.16.0") 
implementation("com.github.bumptech.glide:compose:1.0.0-beta01") 
kapt("com.github.bumptech.glide:compiler:4.16.0")

updated packaging.resources.excludes list: 
excludes += listOf( "META-INF/io.netty.versions.properties", 
"META-INF/DEPENDENCIES", 
"META-INF/LICENSE", 
"META-INF/LICENSE.txt", 
"META-INF/LICENSE.md", 
"META-INF/NOTICE", 
"META-INF/NOTICE.txt", 
"META-INF/NOTICE.md", 
"META-INF/INDEX.LIST" )

for testing: 
androidTestImplementation("androidx.test.ext:junit:1.1.5") 
androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") 
androidTestImplementation("androidx.navigation:navigation-testing:2.8.5")
