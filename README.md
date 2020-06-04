# primes-in-background
A demo of one way to communicate with a background thread in JavaFX

## Requires
* Java 11
* Maven 3.6

## Execution

    mvn clean package
    mvn javafx:run

## What to notice
I use a prime number generator and `Thread.sleep()` to simulate work being done. 

The class `PrimeNumbersTask` extends `Task`, which is JavaFX's implementation of `FutureTask`. The `call` method of `Task` is overridden and its return type is Void (capital "V"). We use the `updateMessage()` and `updateProgress()` methods to communicate with the calling program.  The prime numbers are passed in the `updateMessage()` method. 

### Updating prime numbers
In the calling program (`PrimesInBackground`) there are two ways to read the messages sent from `PrimeNumbersTask`.  One is to bind textProperty of some control, say a Label, to the messageProperty of the task.

    label.textProperty().bind(task.messageProperty());
    
This will update the label with the latest prime number.  But if you want to display a list of all the prime numbers generated, in say a TextArea, you need to add a listener to the task's message property.  The new value will be the message sent, in this case the prime number.

    task.messageProperty().addListener((observable, oldValue, newValue) -> {
        textArea.appendText(newValue + System.lineSeparator());
    });

### Updating progress    
The progress can be used by binding a progress bar's progress property to the task's progress property.
    
    progress.progressProperty().bind(task.progressProperty());
    
### Canceling
The cancel button's onAction sets a field in the `PrimeNumbersTask` (stopTask) to signal a cancel action.  To do this, the cancel button's onAction must be set inside of the start button's onAction.  *(I don't particularly like this.  Can someone think of a better way?)*

Notice that when the stopTask's value becomes true, the progress is set to zero before breaking out of the loop. 
    
## Bugs and improvements
Send pull requests to have your improvements approved.  You can create an issue in GitHub if there are bugs. 