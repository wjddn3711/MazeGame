package mazeGame;

import java.util.*;


public class TreasureExpedition {
    static int a,b; // 보물의 위치를 전역변수로 설정하여 좌표를 기억한다
    static int bound; // 보물 맵의 크기또한 전역 변수로 설정한다
    static int trapCnt; // 보물 맵의 장애물 개수를 저장한다
    static int[][] D = {{-1,0},{1,0},{0,-1},{0,1}};
    static int score; // 맵별 점수 저장
    static String trapBox = String.format("%2s","X");
    static String box = String.format("%2s","O");
    static String treasure = String.format("%2s","❀");
    static class Point {
        int row, col, dist;
        Point(int r, int c, int d){
            row = r;
            col = c;
            dist = d;
        }
    }
    /* 텍스트파일에 이름과 스코어를 기록 가능 하도록 구현 예정
    룰 설명:
    보물섬의 맵 크기를 설정 받는다 ( 소, 중, 대 로 구분하여 받는다 ) 3*3, 5*5, 7*7 을 기준으로 받는다 (각 1점, 2점, 5점 적립)
    각 맵당 장애물이 1, 2, 4개 만들어 진다
    1. 보물의 위치는 매 게임마다 변경된다
    2. 매번 움직일때 마다 턴이 소요되며 최소 이동수에서 +2 까지의 기회가 주어진다
    3. 만약 턴수를 모두 소모했는데도 탈출 할 수 없다면 실패
    4. 만약 보물을 얻었다면 포인트를 얻는다
    5. 보물을 얻거나 턴수를 모두 소모했을 때 더 게임을 진행할 지 묻는다
     */

    // 장애물 생성
    public static ArrayList<int[]> trap (String type){ // 타입을 매개변수로 s,m,l 로 맵의 크기를 받아온다
        Random rand = new Random();
        ArrayList<int[]> traps = new ArrayList<>();
        // s, m, l 의 맵 크기일때의 맵 크기를 전역변수 bound 에 담아준다
        if (type.equals("s")){
            bound = 8;
            trapCnt = 44;
            score = 5;
        }
        else if(type.equals("m")){
            bound = 10;
            trapCnt = 50;
            score = 15;
        }
        else {
            bound = 15;
            trapCnt = 120;
            score = 30;
        }
        while (traps.size()<trapCnt){ // trap 개수 만큼 생성후 종료
            boolean trigger = false;
            int x = rand.nextInt(bound);
            int y = rand.nextInt(bound);
            if((x==0&&y==0)||(x==bound-1&&y==bound-1)) {
                continue; // 만약 시작 지점이거나 도착 지점이라면 pass
            }
            int[] vector = {x,y}; // 벡터에 x,y좌표 삽입
            if (traps.contains(vector)) continue;
//            if (trigger) continue; // 만약 중복되는 값이 있다면 트리거가 발동하여 다음 반복으로 넘어가도록
            traps.add(vector);
        }

        return traps;
    }

    // 보물섬 맵 생성
    public static String[][] tMap (ArrayList<int[]> map) {
        // 트랩을 매개변수로 받아와 트랩을 기준으로 보물섬 맵을 생성한다
        // 리스트에 좌표값들을 모두 담아준다
        String [][] arr = new String[bound][bound];
        // 우선 전체 맵을 O 로 채워준다
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = box;
            }
        }
        // 트랩이 있는 위치는 X 로 바꿔준다
        for (int i = 0; i < map.size(); i++) {
            arr[map.get(i)[0]][map.get(i)[1]] = trapBox; // 리스트의 저장된 좌표(트랩이 있는 곳은) X로 채움
        }
        return arr;
    }

    // 보물의 위치 생성
    public static String[][] fMap (String type, String[][] arr){
        a = bound-1;
        b = bound-1;
        arr[a][b] = treasure; // 보물의 위치는 별로 표시한다
        return arr;
    }

    public static int bfs(int x,int y,String[][] fmap){ // 마지막으로 완성된 맵을 매개변수로 받아와 bfs를 수행한다
        boolean[][] visited = new boolean[bound][bound];
        Queue<Point> q = new LinkedList<>(); // 큐를 링크드리스트 타입으로
        visited[x][y] = true; // 0,0 부터 시작하여 해당 시작좌표를 방문처리한다
        q.add(new Point(x,y,0)); // 큐에 시작 좌표와 움직인 거리를 넣는다
        while (!q.isEmpty()){
            // 큐가 빌때 까지 계속 진행
            Point curr = q.remove();
            if (curr.row==a && curr.col==b) // 만약 목표 좌표라면 거리값을 리턴한다
                return curr.dist;
            for (int i = 0; i < 4; i++) { // 상하좌우 움직였을때의 좌표를 nr,nc 로
                int nr = curr.row + D[i][0], nc = curr.col + D[i][1];
                if (nr < 0 || nr > bound - 1 || nc < 0 || nc > bound - 1) {
                    continue; // 만약 움직인후의 좌료가 맵을 벗어난다면 무시
                }
                if (visited[nr][nc] || fmap[nr][nc].equals(trapBox)) {
                    continue; // 만약 이미 방문한 곳이거나 이동위치가 장애물일때 무시
                }
                visited[nr][nc] = true;
                q.add(new Point(nr, nc, curr.dist + 1)); // 큐에 이동좌표와 거리를 1증가시키고 삽입한다
            }
        }
        return -1; // 해당 위치로 가지 못할경우 -1
    }

    public static String[][] island (String mapType) {
        String[][] island;
        while (true) {
            ArrayList<int[]> traps = trap(mapType);
            island = fMap(mapType, tMap(traps));
            if (bfs(0, 0, island) != -1) {// 만약 제대로 된 맵이 생성되었다면
                break;
            }
        }
        return island;
    }

    // 맵 테스트
//    public static void main(String[] args) {
//        String[][] tm = island("s");
//        mapPrint(tm);
//    }
}
