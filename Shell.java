
import java.io.*;
import java.util.*;


class Shell extends Thread {
    public Shell(){

    }

    public void run(){
        for(int line = 1; ; line++){
            String cmdLine = "";
            do{
                StringBuffer inputBuf = new StringBuffer();
                SysLib.cerr("shell[" + line + "]% ");
                SysLib.cin(inputBuf);
                cmdLine = inputBuf.toString();
            } while(cmdLine.length() == 0);
            String[] args = SysLib.stringToArgs(cmdLine);
            int first = 0;
            for(int i = 0; i < args.length; i++){
                if(args[i].equals(";") || args[i].equals("&") || i == args.length -1){
                    String[] command = generateCmd(args,first, (i == args.length - 1) ? i+1 : i);
                    if(command != null){
                        if(command[0].equals("exit")){ // check if command[0] is "exit". If so, get terminated
                            SysLib.exit();
                            return;
                        }
                        else if (!command[0].equals("exit")){  // otherwise, pass command to SysLib.exec()
                            if(args[i].equals("&")){ // if args[i]="&" don't call SysLib.join(),

                            }
                            // SysLib.exec is executionID
                            else if(!args[i].equals("&") && SysLib.exec(command) != 1){
                                //Otherwise (i.e., ";"), keep calling SysLib.join()
                                while(true){
                                    if(SysLib.join() != SysLib.exec(command) )
                                        continue;
                                }
                            }
                        }

                    }
                    first = i + 1; // go on to the next command delimited by ";" or "&"
                }
            }
        }
    }
    
    /* generateCmd
    * 
    * Passes three arguments: String array args, int first, and int i 
    * 
    * args represents all the arguments typed by the user in the command line
    * 
    * int first is the array index of the first command in args, this changes every time a demlimiter is skipped
    * by  the calcuation first = i + 1
    * 
    * int i is the array index of a delimiter(& or ;) or it's the last element of the array args
    *
    * Return a string of commands without the delimiters or going out of bounds of user-typed command.
    */
    public String[] generateCmd(String[] args, int first, int i) {
        String[] command; // just like args[] but with no delimiters & and ; Only commands
        if(i - first <= 0){ // no commands are typed or illegal command
            return null;
        }

        if(i == args.length){ // in ternary operator for args[], when i == args.length -1, i+1 was done
            command = new String[(i-first) - 1]; // subtract another 1 since i == args.length and counting is from 0
        }
        else{
            command = new String[i-first]; // if args[i] == ";"/"&"
        }

        for(int j = first; j < i; j++){ // first always is a command starting from args[0] to end of args
            // but command[] always starts from 0, so subtraction is needed.
            command[j-first] = args[j];
        }
        return command;
    }

}



