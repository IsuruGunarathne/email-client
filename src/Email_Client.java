//200189M

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

/**
 * It creates a file with the given name
 * 
 * @param name the name of the file to be created
 */
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

/**
 * It takes a file name and a string and writes the string to the file
 * 
 * @param name the name of the file
 * @param text the text to be written
 */
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

/**
 * It deletes the file with the given name
 * 
 * @param name the name of the file to be deleted
 */
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

/**
 * It takes a file name as a string, and returns the number of lines in that file
 * 
 * @param name The name of the file to be read.
 * @return The number of lines in the file.
 */
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

/**
 * It takes a file name as a parameter, reads the first two lines of the file, and returns an array of
 * strings containing the first two lines of the file
 * 
 * @param name the name of the file that contains the credentials
 * @return an array of strings.
 */
    public static String[] getCred (String name){
        String [] returnArr = new String[2];
        try {
            File cred_file = new File(name);
            Scanner filScanner = new Scanner(cred_file);
            String Username = filScanner.nextLine();
            String Password = filScanner.nextLine();
            returnArr[0] = Username;
            returnArr[1] = Password;
            
            filScanner.close();

        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("Invalid login credentials, please check loginCredentidals.txt file");

        }
        return returnArr;
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
/**
 * It takes an array of email addresses, a subject, and a content, and sends an email to the first
 * address in the array, and CC's the rest
 * 
 * @param reciepients an array of email addresses
 * @param subject the subject of the email
 * @param content the body of the email
 */
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
	
	
	// sending email to just one person
/**
 * It takes in a reciepient, subject, and content, and sends an email to the reciepient with the
 * subject and content. 
 * 
 * The first thing we do is add our signoff to the content. 
 * 
 * Then we create a properties object, and set the properties for the email. 
 * 
 * Then we create an authenticator object, which is used to authenticate our email. 
 * 
 * Then we create a session object, which is used to send the email. 
 * 
 * Then we create a MimeMessage object, which is used to create the email. 
 * 
 * Then we set the from, to, subject, and content of the email. 
 * 
 * Then we send the email.
 * 
 * @param reciepient the email address of the person you want to send the email to
 * @param subject the subject of the email
 * @param content the body of the email
 */
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

/**
 * If there's no email in the array sent on the same date (d) with the BirthdaySub subject to the
 * guyMail email, then send the birthday message
 * 
 * @param nBirthEmails number of birthdays on that day
 * @param reciepientsArrList An array list of ibirth objects.
 * @param allMail an arraylist of all the emails sent so far
 * @param d date
 */
    public void sendBirthdayMail(int nBirthEmails, ArrayList<ibirth> reciepientsArrList,ArrayList<email> allMail, String d){
        // implement the individual messages for office friends and personal friends.
        String BirthdaySub = "Happy Birthday!";
        String personalMsg = "Hugs and love on your birthday.\n\n-Isuru Gunarathne";
        String OfficeFriendMsg = "Wish you a Happy Birthday.\n\n-Isuru Gunarathne";
        emailSender BirthdayMailSender = this;
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
    }

/**
 * This function takes in an arraylist of people, a date array, and an arraylist of people with
 * birthdays, and returns an arraylist of people with birthdays today
 * 
 * @param reciepientsArrList An ArrayList of type ibirth. This is the ArrayList that will be returned.
 * @param dateAr an array of the current date in the format of [month, day, year]
 * @param birthdayPeople ArrayList of objects that implement the ibirth interface.
 * @return The method is returning an ArrayList of ibirth objects.
 */
    public ArrayList<ibirth> getBirthdaysToday(ArrayList<ibirth> reciepientsArrList, String[] dateAr, ArrayList<ibirth> birthdayPeople){
        int numBirth = birthdayPeople.size();
        for (int x = 0 ; x<numBirth; x++) {
			String[] birthday = birthdayPeople.get(x).birthday().split("/");
			if (birthday[1].equals(dateAr[1]) && birthday[2].equals(dateAr[2]) ) { 
				reciepientsArrList.add(birthdayPeople.get(x));
			}
			
		}
        return reciepientsArrList;
    }
}

interface people{
	public void setname(String name);
	public String getName();
	public void setemail(String Email);
	public String getEmail();

	
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

    public void setname(String Name){
        this.name = Name;
    }

    public String getName(){
        return this.name;
    }

    public void setemail(String Email){

        // check if format is correct and then set
        this.email=Email;
    }

    public String getEmail(){
        return this.email;
    }

    public void setdes(String design){
        this.designation=design;

    }

    public String getdes(){
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

class personal_friend implements ibirth,people {

    protected String name;
    protected String email;
    private String birthday;
	private String nickname;
    
	
    public personal_friend(String name, String email,String birthday, String nick) {
		this.name= name;
        this.email=email;
        this.birthday = birthday;
		this.nickname = nick;
	}
    
    public void set_birth(String date){
        // check if format is correct
        
    }

    public String birthday(){
        return this.birthday;
    }
    
    public void setemail(String Email){

        // check if format is correct and then set
        this.email=Email;
    }

    public String getEmail() {
    	return this.email;
    }

    public void setname(String Name){
        this.name = Name;
    }
    
    public String getName() {
    	return this.name;
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
	
/**
 * It takes a line from the file and makes a new object of the appropriate type
 * 
 * @param line the line of text that you want to make into a people object
 * @return A people object
 */
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
			// stored: name,nickname,email,birthday
			
			//name,email,birthday,nickname
			temp = new personal_friend(stateVar[0],stateVar[2],stateVar[3],stateVar[1]);
			
			break;
		default :
			
			System.out.println("corrupted entry in ClientList file : " + line); // this is like exception handling
            try {
                temp = new Official(stateVar[0],stateVar[1],stateVar[2]); // try to make a new official from the input line
            } catch (Exception e) {
                System.out.println("this line doesn't have enough variables to make a recipient");
                temp = new Official("this", "is ", "Just a filler"); // if you can't make it, make a filler object instead
            }
			
			// because official is like the most basic type
			
		}
		
		return temp;
		
	}
	
}

class SerialHandler {
	private String fileHandled ;
	
// Creating a new file if it does not exist.
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
	
	
/**
 * It takes an ArrayList of email objects and serializes them to a file
 * 
 * @param toBeSer ArrayList of email objects
 */
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
	
/**
 * It reads the file, and returns an ArrayList of emails
 * 
 * @return An ArrayList of emails
 */
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
        // String loginCred = "loginCredentials.txt";
        // String[] loginCredArr = file_operator.getCred(loginCred);

    	// final String username = loginCredArr[0];
		// final String password = loginCredArr[1];

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
        // also this way i can fill 2 arrays using a single loop, 
        // if I used a method for this, i'll have to get the allPeople array and then loop through it to get the birthdayPeople.
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
		
		ArrayList<ibirth> reciepientsArrList = new ArrayList<ibirth>(); // at most everyone will have birthday today
		
		// today's date;
		Date date = new Date();		
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String d = df.format(date);
        System.out.println("-------------------");
        System.out.println("Email client");
		System.out.println("Date: "+d); // prints todays date
        System.out.println("-------------------");
		String[] dateAr = d.split("/");


		
        emailSender sender1 = new emailSender(username,password,signoff); // this object handles sending normal e-mails
                

        
        // different sender for birthday email (with different sign off)
        
        emailSender BirthdayMailSender = new emailSender (username,password,""); // this object handles sending birthday e-mails

        
        // getting an array of emails of people who have birthdays today
		
        reciepientsArrList = BirthdayMailSender.getBirthdaysToday(reciepientsArrList, dateAr, birthdayPeople);
		
		// we now have an array list of ibirth objects that have birthdays today
        
        int nBirthEmails = reciepientsArrList.size();
        
        // sending the mails
        BirthdayMailSender.sendBirthdayMail(nBirthEmails, reciepientsArrList, allMail,d);        
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
                        System.out.println("Official: <name,email,designation> seperated by commas:");
                        System.out.println("Office_friend: <name,email,designation,birthday> seperated by commas:");
                        System.out.println("Personal: <name,nickname,email,birthday> seperated by commas:");

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
                        try {
                            allPeople.add(factory1.make(in));
                        } catch (Exception e) {
                            System.out.println("------------------------------------------------------------------------");
                            System.out.println("Error occured while creating object. please check the data you entered.");
                            System.out.println("There should be no spaces after the commas");
                            System.out.println("Kindly delete this entry in the client list file");
                            System.out.println("------------------------------------------------------------------------");
                            break;
                        }
                        
                        // note birthday for this guy will be considered the next time we run the program
                        
                        break;
                        
                    case 2:
                        // input format - email, subject, content
                    	System.out.println("enter email, subject, content seperated by commas");
                        System.out.println("If you need to enter more lines to the content enter them bellow");
                        System.out.println("Type zzzz once you have finished entering content");
                    	
                    	String inLine = scanner.nextLine(); // this automatically jumps a line
                    	inLine = scanner.nextLine();  // so i have a new scanner to scan actual input line
                    	String mail_in,Subj,content;
                        try {
                            String[] inArr = inLine.split(", ");
                            mail_in = inArr[0];
                            Subj = inArr[1];
                            content = inArr[2];
                        } catch (Exception e) {
                            System.out.println("------------------------------------------------------------------------");
                            System.out.println("there was an error in the way you entered the details please try again");
                            System.out.println("------------------------------------------------------------------------");
                            break;
                        }

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
                                System.out.println(); // adding a blank line for clarity
                    			peopleThere +=1;
                    		}
                    	}
                    	
                    	if (peopleThere == 0 ) {
                    		System.out.println("There are no people with thier birthday on "+dateNeeded);
                            System.out.println(); // adding a blank line for clarity
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



// I have also uploaded all the files to github for easy reference, use the link bellow
// https://github.com/IsuruGunarathne/email-client
