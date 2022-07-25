## Getting Started

This is an email client which can send emails to a list of recipients stored in a text file.
It also keeps track of all the mail sent in a .SER file
The folder structure follows that of a java project on vscode.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies
- `bin`: the folder to maintain compiled files (.class files)


> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

This project uses `javax.mail.jar` and `activation.jar` as dependencies.

## Functionality
- add recipients
- sending a simple email through a command
- filtering and accessing recipients according to their birthday
- retrieving all emails sent on a particular date
- retrieveing the total number of recipients stored