import java.util.*;

class Node implements Comparator<Node> {
    public int value;
    public int weight;
    public Node() {}
    public Node(int value, int weight) {
        this.value = value;
        this.weight = weight;
    }
    @Override
    public int compare(Node node1, Node node2)
    {
        return Integer.compare(node1.weight, node2.weight);
    }

}
class Graph {
    static ArrayList<Integer> nodeNumber = new ArrayList<>();
    static ArrayList<Integer> visitor = new ArrayList<>();
    int ver;
    LinkedList<Node>[] list;
    HashMap<Integer , int[]> mapDis;
    HashMap<Integer , int[]> mapMin;
    int fairScore;

    public Graph(int ver) {
        this.ver = ver;
        list = new LinkedList[ver];
        mapDis=new HashMap<>();
        for (int i = 0; i < ver; i++) {
            list[i] = new LinkedList<>();
        }
    }

    public void addEdge(int source, int destination, int weight) {
        list[source].addFirst(new Node(destination, weight));
        list[destination].addFirst(new Node(source, weight));
    }

    void DFSUtil(int src, int[] mark) {
        mark[src] = 1;
        System.out.print(src + " ");
        for (Node n : list[src]) {
            if (mark[n.value] != 1) {
                DFSUtil(n.value, mark);
            }
        }


    }

    void DFS() {
        int[] mark = new int[ver];
        for (int i : nodeNumber) {
            if (mark[i] != 1) {
                DFSUtil(i, mark);
            }
        }
    }

    int addVisitor(int k){
        visitor.add(k);
        return update(k);
    }
    int removeVisitor(int k){
        visitor.remove(k);
        return update(k);
    }

    int update(int k){
        int minDis=Integer.MAX_VALUE;
        int node=-1;
        if(visitor.size()==1 || visitor.size()==0){
            fairScore=0;
            node=k;
        }else if(visitor.size()==2){
            fairScore=(mapDis.get(visitor.get(0))[k]);
            node=k;
        }else {
            for (int i = 0; i < visitor.size() ; i++){
                int dist=0;
                for(int j=0 ; j<mapDis.get(visitor.get(i)).length ; j++){
                    dist = dist + mapDis.get(visitor.get(i))[j];
                }
                if(dist < minDis){
                    minDis=dist;
                    node=visitor.get(i);
                }
            }
        }
        return node;
    }
    int[] dijkstra(int src) {
        PriorityQueue<Node> queue = new PriorityQueue<>(ver , new Node());
        Set<Integer> visited = new HashSet<>();
        int[] dist=new int[ver];

        for (int i = 0; i < ver; i++) {
            dist[i] = Integer.MAX_VALUE;
        }

        queue.add(new Node(src, 0));


        dist[src] = 0;
        while (visited.size() != nodeNumber.size()) {
            int u = Objects.requireNonNull(queue.poll()).value;
            visited.add(u);
            for (int i = 0; i < list[u].size(); i++) {
                Node n = list[u].get(i);
                if (!visited.contains(n.value)) {
                    dist[n.value]=Math.min(dist[n.value] , dist[u]+n.weight);
                    queue.add(new Node(n.value, dist[n.value]));
                }
            }
        }
        return dist;
    }
    void printSolution()
    {
        for (int i = 0; i <= nodeNumber.size() ; i++) {
            if (nodeNumber.contains(i)) {
                System.out.println(Arrays.toString(mapDis.get(i)));
            }
        }
    }

    void allDis(){
        for(int i=0 ; i<= nodeNumber.size() ; i++){
            if(nodeNumber.contains(i)){
                mapDis.put(i , dijkstra(i));
            }
        }
//        printSolution();
    }
}



public class Main {

    static ArrayList<Integer> nodeNum=new ArrayList<>();

    static public void test(Graph g){
        g.DFS();
    }
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        int n = in.nextInt() , m = in.nextInt();

        for(int i=0 ; i<n ; i++){
            int node=in.nextInt();
            nodeNum.add(node);
        }
        int size = Collections.max(nodeNum);
        Graph graph=new Graph(size+1);
        Graph.nodeNumber=nodeNum;
        for(int i=0 ; i<m ; i++){
            int u=in.nextInt() , v=in.nextInt() , w=in.nextInt();
            graph.addEdge(u, v, w);
        }
        graph.allDis();

        while (true) {
            String st = in.nextLine();
            if (st.equals("test")) {
                graph.DFS();
            }
            if(st.startsWith("join")){
                String[] index = st.split(" ");
                int number = Integer.parseInt(index[1]);
                System.out.println(graph.addVisitor(number));
            }
            if(st.startsWith("left")){
                String[] index = st.split(" ");
                int number = Integer.parseInt(index[1]);
                System.out.println(graph.removeVisitor(number));
            }
            if(st.equals("exit")){
                break;
            }
        }


    }
}
