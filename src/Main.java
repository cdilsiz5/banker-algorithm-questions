
import java.io.*;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the file address");
        String file=sc.nextLine();
        System.out.println("Enter the new file name");
        String createFile= sc.nextLine();;
        int [] numbers=getNumbers(file);
        int numOfProcesses=numbers[0];
        int numOfResources=numbers[1];
        int [][] maxResource=new int[numOfProcesses][numOfResources];
        int [][] resourceAllocation=new int[numOfProcesses][numOfResources];
        int [] resourceAvailable=new int[numOfResources];
        int [][] needMatrix=new int[numOfProcesses][numOfResources];
        int counter=0;
        int place=2;
        while (place<numbers.length){//fill all matrix from file input
            if(counter<numOfProcesses*numOfResources-1){
                for (int x=0;x<numOfProcesses;x++){
                    for (int j=0;j<numOfResources;j++){
                        maxResource[x][j]=numbers[place];
                        place++;
                        counter++;
                    }
                }
            }
            else if(counter>=numOfProcesses*numOfResources&&counter<numOfProcesses*numOfResources*2){
                for (int x=0;x<numOfProcesses;x++){
                    for (int j=0;j<numOfResources;j++){
                        resourceAllocation[x][j]=numbers[place];
                        place++;
                        counter++;
                    }

                }
            }
            else{
                for (int x=0;x<numOfResources;x++){
                    resourceAvailable[x]=numbers[place];
                    counter++;
                    place++;
                }
            }
        }
        for(int i=0;i<numOfProcesses;i++){//fill need matrix here
            for (int j=0;j<numOfResources;j++){
                needMatrix[i][j]=maxResource[i][j]-resourceAllocation[i][j];
            }
        }

        String safeSequence="";
        int sequenceNumber=0;
        String changeResource="";
        int loopCounter=0;
        boolean deadlock=false;
        boolean usedProccess[]=new boolean[numOfProcesses];//check if process is used

        while (sequenceNumber!=numOfProcesses){
            if(loopCounter==Math.pow(2,numOfProcesses)){// check if algorithm goes deadlock
                 deadlock=true;
                 break;
            }

            for(int i=0;i<numOfProcesses;i++){
                boolean sequence=true;
                for (int j=0;j<numOfResources;j++){
                    if(needMatrix[i][j]-resourceAvailable[j]>0){// check if there is enough space in resource
                        sequence=false;
                    }
                }
                if(sequence&&!usedProccess[i]){
                    char c=(char) (i+64+1);
                    safeSequence+=c;
                    usedProccess[i]=true;
                    sequenceNumber++;
                    for (int x=0;x<numOfResources;x++){
                        resourceAvailable[x]+=-needMatrix[i][x]+maxResource[i][x];//extract need matrix from resource and add max resoursce
                        changeResource+=resourceAvailable[x]+" ";//Add which process is worked

                    }
                    changeResource+="\n";

                }
            }
            loopCounter++;

        }

        String output="Need Matrix : \n"+read(needMatrix)+"\nSafe sequence is :\n"+safeSequence+//string is setted for file output
                "\nChange in available resource matrix : \n"+changeResource;
        if(deadlock)
            output+="\nDeadlock occurs";
        FileWriter myWriter = new FileWriter(createFile);
        myWriter.write(output);
        myWriter.close();

    }
    public static String read(int [] [] array){//reads matrix
        String output="";
        for (int i=0;i<array.length;i++){
            for (int j=0;j<array[0].length;j++){
                output+=(array[i][j]+" ");
            }
            output+="\n";
        }
        return output;
    }

    public static int [] getNumbers(String file) throws IOException{//gets numbers from file
        int [] numbers=new int [2];
        BufferedReader reader=new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file),
                        Charset.forName("UTF-8")));
        int counter=0;
        int content;
        String num="";
        while ((content = reader.read()) != -1) {
            if((content==' '||content=='\n')&&num.length()>0){
                numbers[counter]=Integer.parseInt(num);
                counter++;
                num="";
            }else if (Character.isDigit(content)){
                num+=(char)content;
            }
            if(counter==2){
                numbers= expandCapacity(numbers);
            }
        }
        if(num.length()>0) {
            numbers[counter] = Integer.parseInt(num);
        }
        return numbers;

    }


    public static int [] expandCapacity(int [] array){//expand capatiy
        int [] tempArray=new int[(array[0]*array[1]*2)+array[1]+2];
        for (int i =0;i<array.length;i++){
            tempArray[i]=array[i];
        }
        array=tempArray;
        return array;
    }

}
