# Author

## Your name

Justin Chafe

## Your Student #

3248335

## Your UNB Login

jchafe

## Your GitHub Account Username

jchafeUNB3035

## Any comments or concerns

Three quick things:
1) Decided to break apart my views versus having one giant view.  I prefer the separation.
2) I reused or refactored some code from Dr. Bateman's provided example as it seemed redundant to recreate some of this.
3) When creating shapes, very occasionally it might "feel" that you click and nothing happens. What is happening is that the "MOUSE_DRAGGED" event is being called - the button is pressed while motion is detected. 
It is sensitive but required for "drag selecting" a region when not on a Shape. I left a console message to show this. 
