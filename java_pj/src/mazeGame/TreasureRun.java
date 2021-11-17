package mazeGame;

import mazeGame.TreasureExpedition;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;



public class TreasureRun extends Exception{
    static int pX = 0; // 플레이어의 현재 위치
    static int pY = 0;
    static String trapBox = String.format("%2s","X");
    static String box = String.format("%2s","O");
    static String treasure = String.format("%2s","❀");
    static String player = String.format("%2s","⚉");
    static String[] move = {"u","d","l","r"};
    static int[][] D = {{-1,0},{1,0},{0,-1},{0,1}};
    static ArrayList<String> types =  new ArrayList<String>(){{ // 맵 타입들을 리스트에 저장
        add("s");
        add("m");
        add("l");
    }};
    static ArrayList<String> moves =  new ArrayList<String>(){{ // 맵 타입들을 리스트에 저장
        add("u");
        add("d");
        add("l");
        add("r");
    }};

    static void mapPrint(String[][] island) {
        for (int i = 0; i < island.length; i++) {
            for (int j = 0; j < island[i].length; j++) {
                if (i==pX && j==pY){
                    System.out.print(player);
                }
                else{
                    System.out.print(island[i][j]);
                }
            }
            System.out.println();
        }
    }


    public static void main(String[] args)  throws IOException {
        // 사용할 클래스들
        Scanner sc = new Scanner(System.in);
        User u = new User();
        TreasureExpedition t = new TreasureExpedition();
        // 사용할 변수들
        String intro = "보물섬의 세상에 오신것을 환영합니다\n\n";
        String menu = "메뉴를 선택하세요\n1. 미로게임 시작\n2. 미로게임 종료\n3. 게임 조작법보기\n4. 회원 정보보기";
        String rule = String.format("\n보물섬 세상에는 기본적으로 세가지 타입의 블럭이 있습니다\n%s : 이동할 수 있는 블럭이에요 \n%s : 장애물 이며 이동할 수 없어요 \n%s : 보물의 위치에요! " +
                "\n%s : 플레이어의 위치에요 보물까지 가야겠죠?\n커맨드는 총 u d l r 네가지 존재해요. 꼭 소문자로 입력해주세요 공백없이 커맨드 입력후 enter 하시면" +
                "\n플레이어가 움직여요! 그럼 이제 보물을 찾으러 가볼까요?\n" +
                "\nꉂ ฅ૮( ๑’ꇴ’๑)აฅ｡*ﾟ✧\n\n", box,trapBox,treasure,player);
        String error="\n\n\n해당 위치로는 이동할 수 없습니다. (*꒦ິㅿ꒦ີ)\n\n\n";
        String notValid = "올바른 형식으로 입력해주세요!!\n";

        int choice=0;
        boolean isNew = false;
        String uid = "";

        System.out.println(intro);
        while (true){
            System.out.println("새로운 유저인 경우 1 을 입력해주시고 기존 회원인 경우 2 를 입력해주세요");
            while (true) {
                try {
                    choice = sc.nextInt();
                    break;
                } catch (InputMismatchException e) {//int 외에 다른 type이 들어왔을 경우
                    System.out.println("잘못 입력하셨습니다. 다시 입력 하세요.\n\n새로운 유저인 경우 1 을 입력해주시고 기존 회원인 경우 2 를 입력해주세요");
                } finally {
                    sc.nextLine();// 버퍼 비우기
                }
            }
            if (choice == 1 || choice == 2) break; // 1 이나 2 를 입력받지 않는다면 다시 입력 받도록한다
            System.out.println(notValid);
        }
        // id 입력 및 회원가입
        switch (choice){
            case 1:
                while (true){ // 같은 닉네임으로 신규 회원을 저장할 수 없도록 새로운 id를 받아온다
                    System.out.printf("사용하실 사용자 id를 입력하세요\n" +
                            "ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ\n" +
                            "※ ID 는 영문으로만 입력해주세요\n"+
                            "ID: " );
                    uid = sc.next();
                    System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ\n" );
                    // 현재 경로에 아무런 파일이 없다면 새로 만들어 준다
                    try {
                        isNew = u.isNewUser(uid); // 사용자가 새로운 유저인지 확인
                    } catch(FileNotFoundException e) {
                        PrintWriter pw = new PrintWriter("UserInfo.txt");
                        isNew = true; // 회원 정보가 아무것도 없기 때문에 새로운 유저로 바꿔준다
                        pw.close();
                    }
                    if(u.isNewUser(uid)){ // 현재 같은 닉네임이 존재하지 않는다면
                        break;
                    }
                }
                break;
            case 2:
                while(true){
                    System.out.printf("사용하실 사용자 id를 입력하세요\n" +
                            "ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ\n" +
                            "※ ID 는 영문으로만 입력해주세요\n"+
                            "ID: " );
                    uid = sc.next();
                    System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ\n" );
                    if(u.isNewUser(uid)){
                        System.out.println("사용자 아이디가 존재하지 않습니다.\n다시 한번 입력해주세요!");
                    }
                    else break;
                }
                break;
        }

        if(isNew){
            // 새로운 유저라면
            System.out.printf("\n\n환영합니다. %s 님!!\n",uid);
        }
        else{
            System.out.printf("\n\n다시 오셨군요 %s 님!\n현재 누적 점수는 : %d 입니다\n",uid,u.score);
        }

        System.out.println(intro);
        while (true) {
            pX = 0; // 게임 이용후 플레이어의 위치 초기화
            pY = 0;
            System.out.println(menu);

            // 초이스를 문자로 입력했을 때 오류 방지
            while (true) {
                try {
                    choice = sc.nextInt();
                    break;
                } catch (InputMismatchException e) {//int 외에 다른 type이 들어왔을 경우
                    System.out.println("잘못 입력하셨습니다. 다시 입력 하세요.\n\n" +menu);
                } finally {
                    sc.nextLine();// 버퍼 비우기
                }
            }

            // 미로 게임 종료시
            if (choice==2){
                System.out.println("이용해주셔서 감사합니다.\n다음에 만나요(๑ Ỡ ◡͐ Ỡ๑)ﾉ♡");
                break;
            }
            // 룰 설명 시
            else if (choice==3){
                System.out.printf(rule);
            }
            // 유저 현황
            else if (choice==4){
                System.out.printf("\n현재 %s 님의 점수는 : %d 입니다\n\n",u.getId(),u.getScore());
            }
            // 게임 진행 시
            else if (choice==1){
                boolean isFail = false; // 만약 count를 초과하면 점수가 저장되지 않도록 하기 위한 변수
                int cnt = 0;
                String mapType = "";
                while (true){
                    System.out.println("\n맵의 크기를 설정해주세요 (s,m,l) :");
                    mapType = sc.next();
                    if(types.contains(mapType)) break; // s, m, l 중 하나라면 탈출
                    else {
                        System.out.println("올바른 타입의 맵으로 입력해주세요."); // 아니라면 재입력받도록
                    }
                }
                String[][] island = t.island(mapType); // 보물섬 생성
                int proper = t.bfs(0,0,island);
                System.out.printf("\n맵이 생성 되었습니다!\n        최적의 이동횟수: %d, 이동가능 횟수: %d, 누적이동 횟수: %d\n\n",proper,proper+2,cnt);
                mapPrint(island);
                while (cnt<proper+2) {
                    System.out.println("커맨드를 입력해주세요 (u:up, d:down, l:left, r:right) : ");
                    String command = sc.next();
                    if (!moves.contains(command)){
                        System.out.println("\nu, d, l, r 중 하나의 값을 입력해주세요\n");
                        continue;
                    }
                    int mx = 0, my = 0;
                    for (int i = 0; i < move.length; i++) {
                        if (command.equals(move[i])) {
                            mx = pX + D[i][0]; // 각 상하좌우 일때 좌표의 변화를 나타내는 D값을 mx,my 에 우선 저장해준다
                            my = pY + D[i][1];
                        }
                    }
                    if (mx < 0 || my < 0 || mx >= t.bound || my >= t.bound || island[mx][my].equals(trapBox)) {
                        System.out.println(error); // 범위를 벗어났거나 장애물을 마주치면 에러 메시지 출력
                    } else {
                        pX = mx; // 플레이어를 해당 위치로 이동시켜준다
                        pY = my;
                        cnt++; // 이동 횟수를 증가시킨다
                        System.out.printf("\n\n\n최적의 이동횟수: %d, 이동가능 횟수: %d, 누적이동 횟수: %d\n\n", proper, proper + 2 - cnt, cnt);
                        mapPrint(island);
                        System.out.println("\n\n");
                        if (pX == t.a && pY == t.b) { //보물의 위치에 도달했다면
                            System.out.printf("승리를 축하드립니다. \n%d 점을 얻으셨습니다.\n\n\n", t.score);
                            break; // score를 얻고 탈출
                        }
                    }
                    if (cnt == proper + 2) { //해당 턴수안에 게임을 끝내지 못했다면
                        System.out.println("\n\n저런... 다음 기회를 노려보세요. (;´@̷̶̷̶̷̶̷̶̷̶̷̶̷̶̷̶̷̶̧̢̧̢̧̢̧̢̧̢̧̢̧̢̧̢̧ _ @̷̶̷̶̷̶̷̶̷̶̷̶̷̶̷̶̷̶̧̢̧̢̧̢̧̢̧̢̧̢̧̢̧̢̧`;)\n");
                        isFail = true;
                    }
                }
                if(!isFail){ // 성공하였다면
                    // 신규회원과 구분하여 데이터 저장
                    u.saveUser(uid,t.score,isNew);
                }
                isNew =false; // 더이상 신규회원이 아니니 false로 초기화
            }
        }
    }
}
