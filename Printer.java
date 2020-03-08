public class Printer {
    
        public void print(String message) {
        int time = 200;
        char[] text = message.toCharArray();
        for(char letters :  text) {
            try {
                Thread.sleep(50);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print(letters);
        }
        System.out.println();
        }
    public void printLong(String message) {
        int time = 200;
        char[] text = message.toCharArray();
        for(char letters :  text) {
            try {
                Thread.sleep(400);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print(letters);
        }
        System.out.println();
        }
}