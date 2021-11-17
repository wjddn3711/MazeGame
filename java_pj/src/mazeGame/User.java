package mazeGame;

import java.io.*;

public class User {
    private String id;
    int score;

    public String getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public boolean isNewUser(String id) throws IOException {
        this.id = id; //     생성자 id 저장

        boolean isNew = false; // 새로운 멤버인지 확인할 변수
        BufferedReader br = new BufferedReader(new FileReader("UserInfo.txt"));
        // 파일을 읽어 새로운 유저인지 판단
        while(true) {
            String line = br.readLine();
            if (line == null){
                isNew = true; // 마지막 줄까지 저장된 이름이 없었으므로 새로운 유저가 된다
                this.score = 0; // 새로운 유저이기 때문에 score는 0으로
                break; // 만약 다음 더이상 읽을 텍스트가 없다면 반복종료
            }
            String[] name = line.split(" ");
            if (id.equals(name[0])) {
                this.score = Integer.parseInt(name[1]); // 기존 유저의 점수를 받아온다
                break; // 0번째 인덱스에 저장된 이름이 같기때문에 기존 유저이다.
            }
        }
        br.close();
        return isNew;
    }


    public void saveUser(String id, int newScore, boolean isNew) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader("UserInfo.txt"));
        String dummy="";
        String target="";
        if (isNew){ // 새로운 유저라면 데이터를 추가해준다
            FileWriter fw = new FileWriter("UserInfo.txt",true);
            String data = String.format("%s %d\r\n",this.id,this.score+newScore); // 이름과 기존 점수에 누적 점수를 더하여 저장해준다
            fw.write(data);
            fw.close();
            System.out.println("\n\n점수가 기록되었습니다.\n");
        }
        else{ // 기존 멤버라면 수정후 재작성한다
            while(true) {
                String line = br.readLine();
                if (line == null) {
                    dummy += target ;
                    break; // 만약 다음 더이상 읽을 텍스트가 없다면 반복종료
                }
                if (line.split(" ")[0].equals(id)){
                    // 현재 목표 id 와 동일한 행을 발견했다면
                    target =String.format("%s %d\r\n", id,newScore+this.score);
                    // 기존 점수에 점수를 더하여 덮어쓴다
                }
                else{
                    dummy+=line+"\r\n";
                }
            }
            br.close();
            FileWriter fw = new FileWriter("UserInfo.txt");
            fw.write(dummy);
            fw.close();
            System.out.println("\n\n점수가 기록되었습니다.\n");
        }
        this.score += newScore;
    }
}
