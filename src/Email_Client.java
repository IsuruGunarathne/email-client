//200189M

/* THIS ASSIGNMENT IS GRADED. DO NOT COPY – THIS IS A TURNITIN ASSIGNMENT AND CODE PLAGIARISM WILL BE CHECKED AUTOMATICALLY. There will be a viva for this assignment.

In this assignment, you will be implementing a command-line based email client.

The email client has two types of recipients, official and personal. Some official recipients are close friends.

Details of the recipient list should be stored in a text file.  An official recipient’s record in the text file has the following format: official: <name>, <email>,<designation>. A sample record for official recipients in the text file looks as follows:

Official: nimal,nimal@gmail.com,ceo

A sample record for official friends in the text file looks as follows (last value is the recipient's birthday):

Office_friend: kamal,kamal@gmail.com,clerk,2000/12/12

A sample record for personal recipients in the text file looks as follows (last value is the recipient's birthday):

Personal: sunil,<nick-name>,sunil@gmail.com,2000/10/10

The user should be given the option to update this text file, i.e. the user should be able to add a new recipient through command-line, and these details should be added to the text file. [file handling will be covered in the 11th week]

When the email client is running, an object for each email recipient should be maintained in the application. For this, you will have to load the recipient details from the text file into the application. For each recipient having a birthday, a birthday greeting should be sent on the correct day. Official friends and personal recipients should be sent different messages (e.g. Wish you a Happy Birthday. <your name> for an office friend, and hugs and love on your birthday. <your name> for personal recipients). But all personal recipients receive the same message, and all office friends should receive the same message.  A list of recipients to whom a birthday greeting should be sent is maintained in the application, when it is running. When the email client is started, it should traverse this list, and send a greeting email to anyone having their birthday on that day.

The system should be able to keep a count of the recipient objects. Use static members to keep this count.

All the emails sent out by the email client should be saved into the hard disk, in the form of objects – object serialization can be used for this. The user should be able to retrieve information of all the mails sent on a particular day by using a command-line option. [object serialization will be covered in the 11th week]

You only have to send out messages. No need to implement the logic to receive messages.

Command-line options should be available for:

Adding a new recipient
Sending an email
Printing out all the names of recipients who have their birthday set to current date
Printing out details (subject and recipient) of all the emails sent on a date specified by user input
Printing out the number of recipient objects in the application
You may use the code given in this link to implement the basic functionality of the mail client (But it is perfectly ok to use a different code as well). In the given code, note that it imports the javax.mail package. This package is included in the javax.mail.jar, which can be downloaded from here.

You are given marks for the

Correct implementation of the mail sending functions (i.e. sending a birthday greeting, sending an email based on the instructions given through command-line, ability to serialize email objects,  etc).
Correct use of OOP principles
Use of coding best practices
Use the given Email_Client.java file as the starting point.

Save the recipient data into clientList.txt.

Submission information: Put all the code into one text file, convert it to a pdf, and submit. */


//import libraries


import java.io.*;
import java.util.*;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;


class file_operator{ 
	
	private static int entries=0;
    // this class makes a file with the required name
    // this also handles not creating a file if it already exists.
	
	public static int getEntries() {
		return entries;
	}
	
	public static void setEntries(String name) {
		// use once to set entries = to entries already present
		entries = count_lines(name);
	}

    public static void make_file(String name){
        // creates a file with the given name

        try {
            File the_file = new File(name);
            if (the_file.createNewFile()){
                // new file was created
            }
            else {
                // file already existed, no new file created
            }
        } catch (IOException e) {
            System.out.println("An error occured while creating the ClientList file");
            
        }
        
    }

    public static void write(String name, String text){
        // writes given text into the given file

        // checking if the entry already exists
        boolean duplicated = false;

        try {
            File the_File = new File(name);
            String line;
            
                
            Scanner filScanner= new Scanner(the_File);

            while (filScanner.hasNextLine()){
                line = filScanner.nextLine();
                if (line.equals(text)){
                    duplicated = true;
                    break;
                }
                
            }

            filScanner.close();

        } catch (Exception e) {

            System.out.println("Error occured while checking for duplicates.");

        }
        

        if (duplicated){
            // do nothing entry is already there
        }
        else{
            try {
                FileWriter writer = new FileWriter(name,true);
                writer.write(text+"\n");
                entries+=1;
                writer.close();
            } catch (IOException e) {
                System.out.println("An error occured while writing to the file");
            }
        }

    }

    public static void del_file(String name){
        // deletes the given file
        try {
            File the_File = new File(name);
            if (the_File.delete()){
                // deleted the file
                System.out.println("Deleted the file");
            }
            else{
                // file doesn't exist to delete
                System.out.println("File doesn't exist");
            }
        } catch (Exception e) {

            System.out.println("Error occured while deleing");
        
        }
    }

    public static int count_lines(String name){
        int count = 0;

        try {
            File the_File = new File(name);
            
            Scanner filScanner= new Scanner(the_File);

            while (filScanner.hasNextLine()){
                filScanner.nextLine();
                count++;
                // advance counter by one for each line scanned.
            }

            filScanner.close();

            return count;

        } catch (Exception e) {
            
            System.out.println("Error occured while counting reciepients");
        }

        return 0;
    }

}

class emailSender{
	
	private String username;
	private String password;
	private String signoff;

	
	public emailSender(String username,String password, String signoff) {
		this.username = username;
		this.password = password;
		this.signoff = signoff;
	}
	
	
	// sending email to a batch of reciepients, 1st being To and others cc
	public void sendMail(String[] reciepients,String subject, String content) {

		content+=signoff;  // addin my signoff
		
        Properties prop = new Properties();
        prop.put("mail.smtp.auth",true);
        prop.put("mail.smtp.host","smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.starttls.enable",true);
        prop.put("mail.transport.protocl","smtp");
        
        System.out.println("mail recieved\n");
        
        Authenticator a = new Authenticator() {
        	protected PasswordAuthentication getPasswordAuthentication() {
        		return new PasswordAuthentication(username,password);
        	}
        };
        
        System.out.println("authenticating.....\n");
        Session session = Session.getInstance(prop,a);
        
        
        try {
        	MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username)); 
            // first reciepient will be the TO
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(reciepients[0]));
            
            // other reciepients will be CC
            int n = reciepients.length;
            for (int z = 1; z<n ; z++) {
                msg.addRecipient(Message.RecipientType.CC, new InternetAddress(reciepients[z]));            	
            }


            msg.setSubject(subject);
            msg.setText(content);
            
            //
            //
            System.out.println("sending....\n");
            //
            Transport.send(msg);
            System.out.println("mail was sent to");
            for (int x = 0 ; x < reciepients.length ; x++) {
            	System.out.println(reciepients[x]);
            }
            System.out.println("\n");
            
        }catch (MessagingException e) {
        	System.out.println(e);
        }
	}
	
	
	// ending email to just one person
	public void sendMail(String reciepient,String subject, String content) {

		content+=signoff;  // addin my signoff
		
        Properties prop = new Properties();
        prop.put("mail.smtp.auth",true);
        prop.put("mail.smtp.host","smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.starttls.enable",true);
        prop.put("mail.transport.protocl","smtp");
        
        System.out.println("mail recieved\n");
        
        Authenticator a = new Authenticator() {
        	protected PasswordAuthentication getPasswordAuthentication() {
        		return new PasswordAuthentication(username,password);
        	}
        };
        
        System.out.println("authenticating.....\n");
        Session session = Session.getInstance(prop,a);
        
        
        try {
        	MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username)); 
            // first reciepient will be the TO
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(reciepient));
            

            msg.setSubject(subject);
            msg.setText(content);
            
            //
            //
            System.out.println("sending....\n");
            //
            Transport.send(msg);
            System.out.println("mail was sent to "+reciepient+"\n");

            
        }catch (MessagingException e) {
        	System.out.println(e);
        }
	}
}

interface people{
	public void set_name(String name);
	public String get_name();
	public void set_email(String Email);
	public String get_email();
	public void set_des(String des);
	public String get_des();
	
}

interface ibirth{
    public String birthday(); // returns the date of the birthday
    public String getEmail();
    public String getName();

}

class Official implements people{
    // basic client (Official)
    protected String name;
    protected String email;
    private String designation;
    
    public Official(String name, String email,String des) {
    	this.name = name;
    	this.email = email;
    	this.designation=des;
    }

    public void set_name(String Name){
        this.name = Name;
    }

    public String get_name(){
        return this.name;
    }

    public void set_email(String Email){

        // check if format is correct and then set
        this.email=Email;
    }

    public String get_email(){
        return this.email;
    }

    public void set_des(String design){
        this.designation=design;

    }

    public String get_des(){
        return this.designation;
    }

}

class Office_friend extends Official implements ibirth{

	private String birthday;

    public Office_friend(String name, String email, String des,String birthday) {
		super(name, email, des);
		this.birthday = birthday;
	}
    
    public void set_birth(String date){
        // check if format is correct
        
    }

    public String birthday(){
        return this.birthday;
    }
    
    public String getEmail() {
    	return this.email;
    }
    
    public String getName() {
    	return this.name;
    }
}

class personal_friend extends Office_friend {

	private String nickname;
	
    public personal_friend(String name, String email, String des, String birthday, String nick) {
		super(name, email, des, birthday);
		this.nickname = nick;
	}
    
    

    public void set_nick(String S){
        this.nickname = S;
    }

    public String get_nick(){
        return this.nickname;

    }

}

class email implements Serializable{
	private String reciepients;
	private String subject;
	private String content;
	private String date;
	
	public email(String rec, String sub, String cont,String date) {
		this.reciepients = rec;
		this.subject= sub;
		this.content  = cont;
		this.date = date;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setRecipients(String rec) {
		this.reciepients= rec;
	}
	
	public String getRecipeints() {
		return reciepients;
	}
	
	public void setSubject(String sub) {
		this.subject = sub;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setcontent(String cont) {
		this.content= cont;
	}
	
	public String getContent() {
		return content;
	}
}

class peopleFactory{
	private String factoryName;
	
	public peopleFactory(String name) {
		this.factoryName = name;
	}
	
	public String getName() {
		return factoryName;
	}
	
	public people make(String line) {
		
		// Separating stuff for the line
		String[] lineArr= line.split(" ");
		String[] stateVar = lineArr[1].split(",");
		
		people temp;
		// now have to check what we are making
		
		switch (lineArr[0]) {
		case"Official:":
			// making official
			temp = new Official(stateVar[0],stateVar[1],stateVar[2]);
			
			break;

		case"Office_friend:":
			// name,email,designation,birthday
			temp = new Office_friend(stateVar[0],stateVar[1],stateVar[2],stateVar[3]);
			

			break;
			
		case"Personal:":
			// stored: name,nickname,email,designation,birthday
			
			//name,email,designation,birthday,nickname
			temp = new personal_friend(stateVar[0],stateVar[2],stateVar[3],stateVar[4],stateVar[1]);
			
			break;
		default :
			
			System.out.println("corrupted entry in ClientList file : " + line);
			temp = new Official(stateVar[0],stateVar[1],stateVar[2]);
			// because official is like the most basic type
			
		}
		
		return temp;
		
	}
	
}

class SerialHandler {
	private String fileHandled ;
	
	public SerialHandler(String fileName) {
		
		this.fileHandled = fileName;
		
		// also create file if file is not available
        try {
            File the_file = new File(fileHandled);
            if (the_file.createNewFile()){
                // new file was created
            }
            else {
                // file already existed, no new file created
            }
        } catch (IOException e) {
            System.out.println("An error occured while creating the emailSent.ser file");
            
        }
	}
	
	
	public void SerialEmail(ArrayList<email> toBeSer) {
		// code to store the object
		try {
            FileOutputStream file = new FileOutputStream(fileHandled);
            ObjectOutputStream out = new ObjectOutputStream(file);
              
            out.writeObject(toBeSer);
              
            out.close();
            file.close();
              
            System.out.println("Serialized all emails sent");
			
		}catch(IOException ex) {
			System.out.println("IOException is caught");
		}
		
	}
	
	public ArrayList<email> DeserialEmail() {
		
		// make this return an ArrayList of emails
		ArrayList<email> temp = new ArrayList<email>();
		
		try {
	        FileInputStream file = new FileInputStream(fileHandled);
	        ObjectInputStream in = new ObjectInputStream(file);
	          
	        // Method for deserialization of object
	        temp = (ArrayList<email>) in.readObject();
	          
	        in.close();
	        file.close();
		}catch(IOException | ClassNotFoundException ex)
        {
            System.out.println("IOException is caught");
            System.out.println("This is the first time the program is run");
            System.out.println("Or you just deleted the file containing the sent emails");
        }
		
        return temp;
		
	}
}



public class Email_Client {

    public static void main(String[] args) {
    	
    	Scanner scanner = new Scanner(System.in);
    	String file_name="clientList.txt";

    	
    	
		final String username = "isurujavaemailclient@gmail.com";
		final String password = "xymavauifbyfwxdm";
		String signoff = "\n\nRegards,\nIsuru Gunarathne\n(200189M - CSE 20)";
		
		ArrayList<email> allMail = new ArrayList<email>(); // array to keep all mail sent 
		ArrayList<people> allPeople = new ArrayList<people>();;
		ArrayList<ibirth> birthdayPeople = new ArrayList<ibirth>();;
    	file_operator.setEntries(file_name);
    	
    	// on start up go through all the mail sent and add to allMail array
    	String fileForEmails = "emailSent.ser";
    	SerialHandler emailSerial = new SerialHandler(fileForEmails);
    	
    	allMail = emailSerial.DeserialEmail();  // getting the emails previously sent from serial file.
    	
    	
		// on start up go through all the entries in the ClientList file and make objects for them;
		peopleFactory factory1 = new peopleFactory("factory 1");
		
		// reading from the file
		// didn't use the file handling class because the creating of objects would have 
		// added coupling
		try {
            File the_File = new File(file_name);
            String line;            
                
            Scanner filScanner= new Scanner(the_File);

            while (filScanner.hasNextLine()){
                line = filScanner.nextLine();
                people temp = factory1.make(line);
                
                // add to the array of objects
                allPeople.add(temp);
                
                // conditionally add objects to the birthday people array
                if (temp instanceof ibirth) {
                	ibirth tempBirth = (ibirth) temp; // casting the person as an ibirth person
                	birthdayPeople.add(tempBirth);
                }
                
            }

            filScanner.close();

        } catch (Exception e) {

            System.out.println("Error occured while reading from file and creating objects.");

        }
		
		// now we have all the objects in an array
		// and all the objects with birthdays in a separate array
		
		int numBirth = birthdayPeople.size();
		ArrayList<ibirth> reciepientsArrList = new ArrayList<ibirth>(); // at most everyone will have birthday today
		
		// today's date;
		Date date = new Date();		
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String d = df.format(date);
		System.out.println(d); // prints todays date
		String[] dateAr = d.split("/");
		
		for (int x = 0 ; x<numBirth; x++) {
			String[] birthday = birthdayPeople.get(x).birthday().split("/");
			if (birthday[1].equals(dateAr[1]) && birthday[2].equals(dateAr[2]) ) { 
				reciepientsArrList.add(birthdayPeople.get(x));
			}
			
		}
		
		// we now have an array list of ibirth objects that have birthdays today
		
        emailSender sender1 = new emailSender(username,password,signoff); // this object handles sending normal e-mails
        
        // loop individually and send mail?*************************** to do
        
        // implement the individual messages for office friends and personal friends.
        String BirthdaySub = "Happy Birthday!";
        String personalMsg = "Hugs and love on your birthday.\n\n-Isuru Gunarathne";
        String OfficeFriendMsg = "Wish you a Happy Birthday.\n\n-Isuru Gunarathne";
        
        // different sender for birthday email (with different sign off)
        
        emailSender BirthdayMailSender = new emailSender (username,password,""); // this object handles sending birthday e-mails
        
        int nBirthEmails = reciepientsArrList.size();
        
        // sending the mails
        for (int x = 0 ; x < nBirthEmails ; x++) {
        	ibirth guy = reciepientsArrList.get(x);
        	String greeting;
        	if (guy instanceof personal_friend) {
        		greeting = personalMsg;
        	}
        	else {
        		// office friends
        		greeting = OfficeFriendMsg;
        	}
        	
        	
        	String guyMail = guy.getEmail();
        	
        	// if there's an email in the array sent on 
        	// same date (d)
        	// has the BirthdaySub subject
        	// to the guyMail email
        	// then don't send the birthday message, cause we already sent one.
        	
        	int alreadySent = 0; // set to one if a birthday message has been sent
        	
        	
        	int nMail = allMail.size();
        	for (int f = 0 ; f<nMail ; f++) {
        		email checking = allMail.get(f);
        		String sentAddr = checking.getRecipeints();
        		String subj = checking.getSubject();
        		String sentDate = checking.getDate();
        		
        		if (sentAddr.equals(guyMail) && subj.equals(BirthdaySub) && sentDate.equals(d)) {
        			alreadySent += 1;
         			break; // no need to go further, we found the mail we sent
        		}
        		
        	}
        	
        	if (alreadySent == 0) { // email was not sent
            	BirthdayMailSender.sendMail(guyMail, BirthdaySub, greeting);
            	allMail.add(new email(guyMail,BirthdaySub,greeting,d)); // add the mail we just sent to the list of mail sent
        	}       	

        	
        }
        
        // done, we have sent mails to the people who have birthdays today and added that to the mails send today.        
        
    	
    	while(true) {
            System.out.println("Enter option type: \n"
                    + "1 - Adding a new recipient\n"
                    + "2 - Sending an email\n"
                    + "3 - Printing out all the recipients who have birthdays\n"
                    + "4 - Printing out details of all the emails sent\n"
                    + "5 - Printing out the number of recipient objects in the application\n"
                    + "-1 - Exit");
            
            int option = -2; // program will terminate with a special output if invalid input is encountered
            
            try {
                option = scanner.nextInt();
            }catch (Exception e){
            	System.out.println("Invalid input format");
            }

            
            // getting out of the loop
            if (option == -1) {
            	System.out.println("Terminating program.");
            	break;
            }
            
            if (option == -2) {
            	System.out.println("Terminating program due to exception encountered.");
            	System.out.println("---------------------------------------");
            	System.out.println("Please restart the application");
            	System.out.println("---------------------------------------");
            	break;
            }

            switch(option){
                    case 1:
                        System.out.println("Official: <name, email, designation> seperated by commas:");
                        System.out.println("Office_friend: <name, email, designation, birthday> seperated by commas:");
                        System.out.println("Personal: <name, nickname, email, designation, birthday> seperated by commas:");

                        String head = scanner.next();
                        String data = scanner.next();

                        String in = head +" "+ data; // note: here we're just adding it to the record file,
                        // seperating into different types of objects will happen when reading form file and creating the objects.
                        
                        // input format - Official: nimal,nimal@gmail.com,ceo 
                        // Use a single input to get all the details of a recipient

                        // testing input seperation

                        /* String [] in_arr = in.split(",");
                        for (int x=0;x<3;x++){
                            System.out.println(in_arr[x]);
                        } */
                                        
                        // creating the file
                        file_operator.make_file(file_name);

                        // code to add a new recipient
                        // store details in clientList.txt file

                        file_operator.write(file_name, in );
                        
                        // Hint: use methods for reading and writing files
                        
                        // create and object as well
                        // making the object
                        allPeople.add(factory1.make(in));
                        
                        // note birthday for this guy will be considered the next time we run the program
                        
                        break;
                        
                    case 2:
                        // input format - email, subject, content
                    	System.out.println("enter email, subject, content seperated by commas");
                        System.out.println("If you need to enter more lines to the content enter them bellow");
                        System.out.println("Type zzzz once you have finished entering content");
                    	
                    	String inLine = scanner.nextLine(); // this automatically jumps a line
                    	inLine = scanner.nextLine();  // so i have a new scanner to scan actual input line
                    	
                    	String[] inArr = inLine.split(", ");
                    	
                        String mail_in = inArr[0];
                        String Subj = inArr[1];
                        String content = inArr[2];

                        String currentContent = content;
                        while (true){
                            inLine = scanner.nextLine();
                            if (inLine.equals("zzzz")){
                                break;
                            }
                            else{
                                currentContent += "\n" + inLine;
                            }
                        }   
                        
                        content = currentContent;
                        
                        // for testing 
                        System.out.println("address : "+mail_in);
                        System.out.println("Subject : "+Subj);
                        System.out.println("content : "+content);
                        
                        // end testing

                        // code to send an email
                        sender1.sendMail(mail_in, Subj, content);
                        allMail.add(new email(mail_in,Subj,content,d)); // add the mail we just sent to the list of mail sent
                        // point to note: the mail objects in allMail arrayList don't have sign offs, just the content is stored.
                        // we will serialize all e mails sent once the program ends.
                        
                        break;
                    case 3:
                        // input format - yyyy/MM/dd (ex: 2018/09/17)
                        // code to print recipients who have birthdays on the given date
                    	System.out.println("Enter the date needed, format yyyy/MM/dd ");
                    	String dateNeeded = scanner.next();

                    	int nPeople = birthdayPeople.size();
                    	int peopleThere = 0;
                    	for (int x = 0; x <nPeople; x++) {
                    		ibirth person = birthdayPeople.get(x);
                    		String hisBirthday = person.birthday();
                    		if (dateNeeded.equals(hisBirthday)) {
                    			System.out.println(person.getName()+" has a birthday on "+ dateNeeded);
                    			peopleThere +=1;
                    		}
                    	}
                    	
                    	if (peopleThere == 0 ) {
                    		System.out.println("There are no people with thier birthday on "+dateNeeded);
                    	}
                    	
                        break;
                        
                    case 4:
                    	
                        // input format - yyyy/MM/dd (ex: 2018/09/17)
                        // code to print the details of all the emails sent on the input date
                    	
                    	System.out.println("Enter the date to retrieve emails, format yyyy/MM/dd ");
                    	String dateIn = scanner.next();
                    	int nEmails = allMail.size();

                    	System.out.println("Date entered: "+dateIn);
                    	int sentNum = 0;
                    	
                    	for (int x = 0; x < nEmails; x++) {
                    		email current = allMail.get(x);
                    		
                    		if (current.getDate().equals(dateIn)) {
                            	System.out.println("---------------------------------------");
                    			System.out.println("Mail number "+(sentNum+1)+" on "+dateIn);
                    			// this was sent on the needed date
                    			System.out.println("To: "+current.getRecipeints());
                    			System.out.println("Subject: "+current.getSubject());
                    			System.out.println("Content: "+current.getContent());
                    			sentNum+=1;
                    		}
                    	}
                    	
                    	if (sentNum == 0 ) {
                    		System.out.println("---------------------------------------");
                    		System.out.println("No emails were send on "+dateIn);
                    	}
                    	System.out.println(); // blank line for elegance
                    	
                    	
                        break;

                    case 5:
                    	
                        // code to print the number of recipient objects in the application
                        int n_reciepients = file_operator.getEntries();
                        System.out.println("The number of reciepients is : "+n_reciepients);
                        break;
                    
                    default:
                        System.out.println("Invalid Selection, please enter a valid selection");
                        break;
                    	
            	}

            // start email client
            // code to create objects for each recipient in clientList.txt
            // use necessary variables, methods and classes
                                    
	    	}
    	
        ////// Serializing all e mails sent
        ////// I will store the array of emails as one object
    	
    		emailSerial.SerialEmail(allMail);
	    	scanner.close();
	    	
      }
      
}

// create more classes needed for the implementation (remove the  public access modifier from classes when you submit your code)
