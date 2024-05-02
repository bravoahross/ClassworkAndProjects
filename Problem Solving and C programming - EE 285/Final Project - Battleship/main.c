#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <string.h>

int main(void) {
  int grid;
  int ships;
  int ships2;
  int comShipTotal;
  int userShipTotal;
  int i;
  int j;
  int R;
  int C;
  int K;
  int ret;
  int ret2;
  int ret3;
  int cnum;
  char cname[20];
  char name[20];
  srand(time(NULL));
  printf("Welcome to battleship!\n");
  printf("What is your name? (no spaces)\n"); //takes your name #6
  scanf("%s", name);
  cnum = rand() % 3;
  if (cnum == 0) {              // picks a random CPU name #6
    strcpy(cname, "Pirate Pete");
  }
  else if(cnum ==1) {
    strcpy(cname, "Admiral Ackbar");
  }
  else if(cnum ==2) {
    strcpy(cname, "Isoroku Yamamoto");
  }
  printf("What size grid would you like? (min 3) ");
  scanf("%d", &grid);          // determines grid size #1
  printf("How many ships would you like? (min 2) ");
  scanf("%d", &ships);         // determines ship ammount #1
  printf("\n");

  const int size = grid;        //Creates 3 grids to use
  char *userBoard[size][size];
  char *comBoard[size][size];
  char *comVisible[size][size]; //#4

  for(i = 0; i < size; ++i){
    for(j = 0; j < size; ++j){
      comBoard[i][j] = "  ?  ";
    }
  }
  
  printf("%s's fleet: \n", cname);        //print origional comBoard
  for(i = 0; i < size; ++i){
    for(j = 0; j < size; ++j){
      printf(" %s" , &*comBoard[i][j]);
    }
    printf("\n");
    printf("\n");
  }

  printf("\n");                         //print comboard, but calls it your board
  printf("%s's fleet: \n", name);    
  for(i = 0; i < size; ++i){
    for(j = 0; j < size; ++j){
      printf(" %s" , &*comBoard[i][j]);
    }
    printf("\n");
    printf("\n");
  }
  for(i = 0; i < size; ++i){      //players board
    for(j = 0; j < size; ++j){
      userBoard[i][j] = comBoard[i][j];
    }
  }

  for(i = 0; i < size; ++i){      //computer visible board
    for(j = 0; j < size; ++j){
      comVisible[i][j] = comBoard[i][j];
    }
  }

  ships2 = ships;         //assigns ship totals
  comShipTotal = ships;
  userShipTotal = ships;
  while (ships2 > 0){   
    i = 0;
    while(i == 0){                  // places computer's ships randomly #1
    R = rand() % size;
    C = rand() % size;
    ret3 = strncmp(userBoard[R][C], "x", 5);
    if (ret3 != 0){               // makes it so a ship can't be placed on top of another  #5
    i = 1;
    }
    }
    comBoard[R][C] = "x";
    ships2 = ships2 - 1;
  }
  printf("\n");
  
  printf("Where do you want to put your ships?\n"); //place ships #1
  while (ships > 0){
    printf("Point:\n");
    K = 0;
    while (K == 0){
    printf("X: (0 to %d) ", size-1); //check collum
    scanf("%d", &C);
    if (C <= size -1) {
      break; 
    }
    else {
      printf("Out of range\n"); //confirms validity of placement #5
    }
    }
    while (K == 0){
    printf("Y: (0 to %d) ", size-1); //check row
    scanf("%d", &R);
    if (R <= size -1) {
      break; 
    }
    else {
      printf("Out of range\n"); 
    }
    }
    printf("\n");
    userBoard[R][C] = " Ship";
    ships = ships - 1;
  }
  printf("%s's grid: \n", name);       //print board with ships
  printf("------------\n");
  for(i = 0; i < size; ++i){
    for(j = 0; j < size; ++j){
      printf(" %s" , &*userBoard[i][j]);
    }
    printf("\n");
    printf("\n");
  }
  printf("\n");
  
  

int turn;
turn = rand() % 2;  // creates 2 outcomes to use in "switch"

switch(turn) {   //#7

   case 0  :
      //code for player
      while(userShipTotal && comShipTotal != 0) {//Loop for turns starts here

  printf("Your turn. Take a shot!\n");             //player's turn #2
  printf("\n");
  while (K == 0){
  printf("X: (0 to %d) ", size-1);
  scanf("%d", &C);
  if (C <= size -1){
    break;
  }
  else {
    printf("Out of range\n");          //determines validity of placement of X #5
  }
  }
  while (K == 0){
  printf("Y: (0 to %d) ", size-1);
  scanf("%d", &R);
  if (C <= size -1){
    break;
  }
  else {
    printf("Out of range\n");         //determines validity of placement of Y #5
  }
  }
  printf("\n");
  if(*comBoard[R][C] == 'x'){
    comVisible[R][C] = " Hit!";
    comShipTotal = comShipTotal - 1;  // tracks ships left
    comBoard[R][C] = "  ?  ";
  }
  else{
    comVisible[R][C] = "Miss!"; // #3
  }
  printf("%s's fleet: \n", cname); 
  printf("-----------------\n");
  for(i = 0; i < size; ++i){
    for(j = 0; j < size; ++j){
      printf(" %s", &*comVisible[i][j]);
    }
    printf("\n");
    printf("\n");
  }
  printf("\n");

  if(comShipTotal == 0){
    printf("Game over!\n");         // ends game
  }
  else {
    printf("Computer's turn!\n");               //computer's turn #2
    i = 0;
    while( i == 0){
    R = rand() % size;
    C = rand() % size;
    ret = strncmp(userBoard[R][C], " Ship", 5);
    ret2 = strncmp(userBoard[R][C], "Miss!", 5); //#3
    ret3 = strncmp(userBoard[R][C], " Hit!", 5);
    if(ret2 != 0 && ret3 != 0){
      i++;
    }
    }
    if (ret == 0){
      userBoard[R][C] = " Hit!";
      userShipTotal = userShipTotal - 1;
    }
    else if(ret2 == 0){
      userBoard[R][C] = " Hit!";
    }
    else{
      userBoard[R][C] = "Miss!";
    }
    printf("Computer guesses (%d,%d)\n", C,R);
    printf("\n");
    printf("%s's fleet: \n", name);       //print board with ships
  printf("------------\n");
  for(i = 0; i < size; ++i){
    for(j = 0; j < size; ++j){
      printf(" %s" , &*userBoard[i][j]);
    }
    printf("\n");
    printf("\n");
  }
  }
  printf("\n");
  printf("\n");
  }
      break; /* optional */
	
   case 1  :
      //code for computer
      while(userShipTotal && comShipTotal != 0) {//Loop for turns starts here
    printf("Computer's turn!\n");               //computer's turn
    i = 0;
    while( i == 0){
    R = rand() % size;
    C = rand() % size;
    ret = strncmp(userBoard[R][C], " Ship", 5);
    ret2 = strncmp(userBoard[R][C], "Miss!", 5);
    ret3 = strncmp(userBoard[R][C], " Hit!", 5);
    if(ret2 != 0 && ret3 != 0){
      i++;
    }
    }
    if (ret == 0){
      userBoard[R][C] = " Hit!";
      userShipTotal = userShipTotal - 1;
    }
    else if(ret2 == 0){
      userBoard[R][C] = " Hit!";
    }
    else{
      userBoard[R][C] = "Miss!";
    }
    printf("Computer guesses (%d,%d)\n", C,R);
    printf("\n");
    printf("%s's fleet: \n", name);       //print board with ships
    printf("------------\n");
    for(i = 0; i < size; ++i){
    for(j = 0; j < size; ++j){
      printf(" %s" , &*userBoard[i][j]);
    }
    printf("\n");
    printf("\n");
  }
  
  printf("\n");
  printf("\n");
  if(userShipTotal == 0){
    printf("Game over!\n");
  }
  if(userShipTotal != 0) {
  printf("Your turn. Take a shot!\n");             //player's turn
  printf("\n");
  printf("X: (0 to %d) ", size-1);
  scanf("%d", &C);
  printf("Y: (0 to %d) ", size-1);
  scanf("%d", &R);
  printf("\n");
  if(*comBoard[R][C] == 'x'){
    comVisible[R][C] = " Hit!";
    comShipTotal = comShipTotal - 1;
    comBoard[R][C] = "  ?  ";
  }
  else{
    comVisible[R][C] = "Miss!";
  }
  printf("%s's fleet: \n", cname); 
  printf("-----------------\n");
  for(i = 0; i < size; ++i){
    for(j = 0; j < size; ++j){
      printf(" %s", &*comVisible[i][j]);
    }
    printf("\n");
    printf("\n");
  }
  printf("\n");

  if(comShipTotal == 0){
    printf("Game over!\n");
  
      }
      
      }
      }
      break; /* optional */
  


return 0;
}
if(userShipTotal == 0){
    printf("You lost!");
  }
  else if(comShipTotal == 0){
  printf("You won!\n");
  printf("        $$$$\n       $$  $\n       $   $$              \n       $   $$\n       $$   $$\n        $    $$\n        $$    $$$\n         $$     $$\n         $$      $$\n          $       $$\n    $$$$$$$        $$\n  $$$               $$$$$$\n $$ $$$$               $$$\n $   $$$  $$$            $$\n $$        $$$            $\n  $$    $$$$$$            $\n  $$$$$$$    $$           $\n  $$       $$$$           $\n   $$$$$$$$$  $$         $$\n    $        $$$$     $$$$\n    $$    $$$$$$    $$$$$$\n     $$$$$$    $$  $$\n       $     $$$ $$$\n        $$$$$$$$$$\n");
  }
}