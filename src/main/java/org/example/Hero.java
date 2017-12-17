package org.example;

import java.util.Stack;



public class Hero implements Comparable<Hero> , Runnable{

  private int score;
  private Graph graph;
  private String name;


  Hero(Graph graph) {
    this.graph = graph;
    score = 0;
    name = "default";
  }

  Hero(String name, Graph graph) {
    this.graph = graph;
    score = 0;
    this.name = name;
  }

  public int play() {
    int taille = graph.getGraphSize();
    boolean[] visited = new boolean[taille];

    Room room = graph.getRoom(graph.randomRoom(0, taille));

    Stack<Room> stack = new Stack<Room>();
    stack.push(room);
    visited[room.getId()] = true;
    while (!stack.isEmpty()) {
      Room currentRoom = stack.pop();

      //System.out.println(currentRoom.id + " " + currentRoom.getAdjList());
      this.score += currentRoom.loot();
      for (Room r : currentRoom.getAdjList()) {

        if (!visited[r.getId()]) {
          visited[r.getId()] = true;
          stack.push(r);
        }
      }
    }
    return this.score;
  }

  public String toString() {
    return ("The player " + this.name + " has " + this.score + " treasure(s)");
  }

  @Override
  public int compareTo(Hero other) {
    if (this.score - other.getScore()!= 0)
      return -(this.score - other.getScore());
    else
      return this.name.compareTo(other.getName());
  }

  public int getScore() {
    return score;
  }
  
  public String getName() {
    return name;
  }
  @Override
  public void run() {
    play(); 
  }

}
