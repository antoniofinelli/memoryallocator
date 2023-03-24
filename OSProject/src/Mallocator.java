import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Mallocator {

    public static void main(String[] args) throws FileNotFoundException {
        //Get all memory locations from given dataset
        memBlock[] memoryFF = getMinput("src/Minput.data");
        memBlock[] memoryBF = getMinput("src/Minput.data");
        memBlock[] memoryWF = getMinput("src/Minput.data");

        //get all memory locations from custom dataset 1
        memBlock[] memoryFF1 = getMinput("src/Minput1.data");
        memBlock[] memoryBF1 = getMinput("src/Minput1.data");
        memBlock[] memoryWF1 = getMinput("src/Minput1.data");

        //get all memory locations from custom dataset 2
        memBlock[] memoryFF2 = getMinput("src/Minput2.data");
        memBlock[] memoryBF2 = getMinput("src/Minput2.data");
        memBlock[] memoryWF2 = getMinput("src/Minput2.data");


        //Get all processes from given test set
        process[] processesFF = getProcesses("src/Pinput.data");
        process[] processesBF = getProcesses("src/Pinput.data");
        process[] processesWF = getProcesses("src/Pinput.data");

        //Get all processes from custom test set 1
        process[] processesFF1 = getProcesses("src/Pinput1.data");
        process[] processesBF1 = getProcesses("src/Pinput1.data");
        process[] processesWF1 = getProcesses("src/Pinput1.data");

        //Get all processes from custom test set 2
        process[] processesFF2 = getProcesses("src/Pinput2.data");
        process[] processesBF2 = getProcesses("src/Pinput2.data");
        process[] processesWF2 = getProcesses("src/Pinput2.data");



        //Memory allocation for given data set
        ff(memoryFF,processesFF, "src/FFoutput.data");
        bf(memoryBF, processesBF, "src/BFoutput.data");
        wf(memoryWF,processesWF, "src/WFoutput.data");

        //Memory allocation for custom set 1
        ff(memoryFF1,processesFF1, "src/FFoutput1.data");
        bf(memoryBF1, processesBF1, "src/BFoutput1.data");
        wf(memoryWF1,processesWF1, "src/WFoutput1.data");

        //Memory allocation for custom set 2
        ff(memoryFF2, processesFF2, "src/FFoutput2.data");
        bf(memoryBF2, processesBF2, "src/BFoutput2.data");
        wf(memoryWF2, processesWF2, "src/WFoutput2.data");

    }

    //Function to read and store memory
    public static memBlock[] getMinput(String fileName){
        //Use array to store memorySlots since we have the amount of slots
        memBlock[] memory = new memBlock[0];

        //Information for file read in from https://www.w3schools.com/java/java_files_read.asp
        try {
            //Set the target file to mInput
            File mInput = new File(fileName);
            Scanner scanner = new Scanner(mInput);

            //Flag to mark if first iteration
            boolean firstIteration = true;
            //Counter to select which index the memory should be inserted
            int counter = 0;

            while (scanner.hasNextLine()){
                String in = scanner.nextLine();

                //If it's the first iteration, set memory to new array with length from first line of file
                if(firstIteration){
                    memory = new memBlock[Integer.parseInt(in)];
                    firstIteration = false;
                }else{
                    //Split on whitespace to get memory addresses
                    String[] memLocs = in.split(" ");
                    //Create new mem block with info from file
                    memory[counter] = new memBlock(Integer.parseInt(memLocs[0]),Integer.parseInt(memLocs[1]));

                    ++counter;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return memory;
    }

    //Function to read and store processes
    public static process[] getProcesses(String fileName){
        //Use array to store memorySlots since we have the amount of slots
        process[] processes = new process[0];

        //Information for file read in from https://www.w3schools.com/java/java_files_read.asp
        try {
            //Set the target file to mInput
            File mInput = new File(fileName);
            Scanner scanner = new Scanner(mInput);

            //Flag to mark if first iteration
            boolean firstIteration = true;
            //Counter to select which index the process should be inserted
            int counter = 0;

            while (scanner.hasNextLine()){
                String in = scanner.nextLine();

                //If it's the first iteration, set processes to new array with length from first line of file
                if(firstIteration){
                    processes = new process[Integer.parseInt(in)];
                    firstIteration = false;
                }else{
                    //Split on whitespace to get memory addresses
                    String[] memLocs = in.split(" ");
                    //Create new mem block with info from file
                    processes[counter] = new process(Integer.parseInt(memLocs[0]),Integer.parseInt(memLocs[1]));

                    ++counter;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return processes;
    }

    public static void ff(memBlock[] memory, process[] processes, String output){
        //Linked list containing all processes with no slot
        LinkedList<process> rejectedProcesses = new LinkedList<process>();
        //Loop through all processes
        for(int i = 0; i < processes.length; ++i){
            //If a slot is found, do not insert into other slots
            boolean targetReached = false;
            for(int j = 0; j < memory.length; ++j){
                //Check if slot is empty
                if(memory[j].getSize() - memory[j].getOccupiedSpace() >= processes[i].getSize()&& !targetReached){
                    //Check if size of memory slot is large enough and that target has not been reached

                        //Add the process and set target reached to true
                        memory[j].addProcess(processes[i]);
                        targetReached = true;

                }
                //If no target was ever reached, add the process to the rejected list
            }if(targetReached == false){
                rejectedProcesses.add(processes[i]);
            }
        }

        try{
            //File writing code from https://www.baeldung.com/java-write-to-file
            File mInput = new File(output);
            FileWriter writer = new FileWriter(mInput);
            PrintWriter printer = new PrintWriter(writer);
            int currEnd = 0;
            for(int i = 0; i < memory.length; ++i){
                //Write output to file if current memory location has process
                boolean first = true;
                for(process p: memory[i].getProcessList()){

                    if(first == true){
                        printer.printf("%d %d %d \n", memory[i].getStartAddress(), memory[i].getStartAddress() + p.getSize(), p.getId());
                        currEnd = memory[i].getStartAddress() + p.getSize();
                        first = false;
                    }else{
                        printer.printf("%d %d %d \n",  memory[i].getStartAddress() + memory[i].getProcessList().get(0).getSize(), currEnd + p.getSize(), p.getId());
                    }

                }
                /*
                if(memory[i].getProcessList().size() > 0){
                    printer.printf("%d %d %d \n", memory[i].getStartAddress(), memory[i].getStartAddress() + memory[i].getProcessList().get(0).getSize(), memory[i].getProcessList().get(0).getId());
                }*/
            }
            //At the end of the data, add the finish status
            if(rejectedProcesses.size() != 0){
                printer.printf("-");
                //Iterate through all rejected processes
                for(int i = 0; i < rejectedProcesses.size(); ++i){
                    printer.print(rejectedProcesses.get(i).getId());

                    //Print comma if not the last process
                    if(i+1 != rejectedProcesses.size()){
                        printer.print(",");
                    }
                }
            }else{
                printer.print("-" + rejectedProcesses.size());
            }

            printer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void bf(memBlock[] memory, process[] processes, String output){

        //Linked list containing all processes with no slot
        LinkedList<process> rejectedProcesses = new LinkedList<process>();
        //Loop through all processes
        for(int i = 0; i < processes.length; ++i){
            //The current best fit calculated by memory size - process size
            int currentBest = 999999;
            //The index that is currently the best fit
            int currBestIndex = -1;
            for(int j = 0; j < memory.length; ++j){
                if(memory[j].getSize() - memory[j].getOccupiedSpace() >= processes[i].getSize()){
                    //Check if size of memory slot is large enough and that target has not been reached
                    if(memory[j].getSize() - processes[i].getSize() < currentBest && memory[j].getSize() - processes[i].getSize() >= 0){
                        //Add the process and set target reached to true
                        currentBest = memory[j].getSize() - processes[i].getSize();
                        currBestIndex = j;
                    }
                }

                //If no target was ever reached, add the process to the rejected list
            }if(currBestIndex == -1){
                rejectedProcesses.add(processes[i]);
            }else{
                memory[currBestIndex].addProcess(processes[i]);

            }
        }



        try{
            //File writing code from https://www.baeldung.com/java-write-to-file
            File mInput = new File(output);
            FileWriter writer = new FileWriter(mInput);
            PrintWriter printer = new PrintWriter(writer);
            int currEnd = 0;
            for(int i = 0; i < memory.length; ++i){
                //Write output to file if current memory location has process
                boolean first = true;
                for(process p: memory[i].getProcessList()){

                    if(first == true){
                        printer.printf("%d %d %d \n", memory[i].getStartAddress(), memory[i].getStartAddress() + p.getSize(), p.getId());
                        currEnd = memory[i].getStartAddress() + p.getSize();
                        first = false;
                    }else{
                        printer.printf("%d %d %d \n",  memory[i].getStartAddress() + memory[i].getProcessList().get(0).getSize(), currEnd + p.getSize(), p.getId());
                    }

                }
                /*
                if(memory[i].getProcessList().size() > 0){
                    printer.printf("%d %d %d \n", memory[i].getStartAddress(), memory[i].getStartAddress() + memory[i].getProcessList().get(0).getSize(), memory[i].getProcessList().get(0).getId());
                }*/
            }
            //At the end of the data, add the finish status
            if(rejectedProcesses.size() != 0){
                printer.printf("-");
                //Iterate through all rejected processes
                for(int i = 0; i < rejectedProcesses.size(); ++i){
                    printer.print(rejectedProcesses.get(i).getId());

                    //Print comma if not the last process
                    if(i+1 != rejectedProcesses.size()){
                        printer.print(",");
                    }
                }
            }else{
                printer.print("-" + rejectedProcesses.size());
            }

            printer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void wf(memBlock[] memory, process[] processes, String output){

        //Linked list containing all processes with no slot
        LinkedList<process> rejectedProcesses = new LinkedList<process>();
        //Loop through all processes
        for(int i = 0; i < processes.length; ++i){
            //The current best fit calculated by memory size - process size
            int currentWorst = 0;
            //The index that is currently the best fit
            int currBestIndex = -1;
            for(int j = 0; j < memory.length; ++j){
                //Check if slot is empty
                if(memory[j].getSize() - memory[j].getOccupiedSpace() >= processes[i].getSize()){
                    //Check if size of memory slot is large enough and that target has not been reached
                    if(memory[j].getSize() - processes[i].getSize() > currentWorst && memory[j].getSize() - processes[i].getSize() > 0){
                        //Add the process and set target reached to true
                        currentWorst = memory[j].getSize() - processes[i].getSize();
                        currBestIndex = j;
                    }
                }

                //If no target was ever reached, add the process to the rejected list
            }if(currBestIndex == -1){
                rejectedProcesses.add(processes[i]);
            }else{
                memory[currBestIndex].addProcess(processes[i]);

            }
        }


        try{
            //File writing code from https://www.baeldung.com/java-write-to-file
            File mInput = new File(output);
            FileWriter writer = new FileWriter(mInput);
            PrintWriter printer = new PrintWriter(writer);
            int currEnd = 0;
            for(int i = 0; i < memory.length; ++i){
                //Write output to file if current memory location has process
                boolean first = true;
                for(process p: memory[i].getProcessList()){

                    if(first == true){
                        printer.printf("%d %d %d \n", memory[i].getStartAddress(), memory[i].getStartAddress() + p.getSize(), p.getId());
                        currEnd = memory[i].getStartAddress() + p.getSize();
                        first = false;
                    }else{
                        printer.printf("%d %d %d \n",  memory[i].getStartAddress() + memory[i].getProcessList().get(0).getSize(), currEnd + p.getSize(), p.getId());
                    }

                }
                /*
                if(memory[i].getProcessList().size() > 0){
                    printer.printf("%d %d %d \n", memory[i].getStartAddress(), memory[i].getStartAddress() + memory[i].getProcessList().get(0).getSize(), memory[i].getProcessList().get(0).getId());
                }*/
            }
            //At the end of the data, add the finish status
            if(rejectedProcesses.size() != 0){
                printer.printf("-");
                //Iterate through all rejected processes
                for(int i = 0; i < rejectedProcesses.size(); ++i){
                    printer.print(rejectedProcesses.get(i).getId());

                    //Print comma if not the last process
                    if(i+1 != rejectedProcesses.size()){
                        printer.print(",");
                    }
                }
            }else{
                printer.print("-" + rejectedProcesses.size());
            }

            printer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static class process{
        private int id;
        private int size;

        public process(int id, int size){
            setId(id);
            setSize(size);
        }

        public void setId(int id){
            this.id = id;
        }

        public int getId(){
            return id;
        }

        public void setSize(int size){
            this.size = size;
        }

        public int getSize(){
            return size;
        }

        @Override
        public String toString() {
            return "Process ID, Size: " + getId() + ","+ getSize();
        }
    }

    public static class memBlock{
        //Memory will be stored as [startAddress, endAddress]
        private int[] memory = new int[2];
        LinkedList<process> pList = new LinkedList<process>();

        //Constructor to set start and end size
        memBlock(int startAddress, int endAddress){
            setStartAddress(startAddress);
            setEndAddress(endAddress);
        }

        //Get total memory size
        public int getSize(){
            return memory[1] - memory[0];
        }

        public int getOccupiedSpace(){
            //Running total of occupied space
            int total = 0;
            for(process p: pList){
                total+=p.getSize();
            }
            return total;
        }

        //Set start address
        public void setStartAddress(int startAddress){
            this.memory[0] = startAddress;
        }

        //Set end address
        public void setEndAddress(int endAddress){
            this.memory[1] = endAddress;
        }

        //Get start address
        public int getStartAddress(){
            return memory[0];
        }

        //Get end address
        public int getEndAddress(){
            return memory[1];
        }

        public LinkedList<process> getProcessList() {
            return pList;
        }

        public void addProcess(process p){
            pList.add(p);
        }

        @Override
        public String toString() {
            return "Memory location : " + Arrays.toString(memory);
        }
    }
}
