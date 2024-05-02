#include<stdio.h>
#include<string.h>
#include<stdlib.h>
#include<unistd.h>
#include<sys/types.h>
#include<sys/wait.h>
  
// Greeting shell during startup
void init_shell()
{
    printf("\n\n==============================");
    printf("\n\n\tNOAH'S SHELL");
    printf("\n\n==============================");
    char* username = getenv("USER");
    printf("\n\nUSER is: @%s", username);
    printf("\n");
}

// Function Declarations for builtin shell commands:
int cd(char **args);
int exitShell(char **args);
int pid(char **args);
int ppid(char **args);
int pwd(char **args);
int amper = 0;

// builtin shell comand list:
char *builtin_str[] = {
  "cd",
  "exit",
  "pid",
  "ppid",
  "pwd"
};

int (*builtin_func[]) (char **) = {
  &cd,
  &exitShell,
  &pid,
  &ppid,
  &pwd
};

// cd (change directory) function:
int cd(char **args)
{
  if (args[1] == NULL) {
    fprintf(stderr, "NoahShell: expected argument to \"cd\"\n");
  } else {
    if (chdir(args[1]) != 0) {
      perror("NoahShell: 1");
    }
  }
  return 1;
}

// exit function:
int exitShell(char **args)
{
  return 0;
}

// pid function:
int pid(char **args)
{
  pid_t pid = getpid();
  fprintf(stderr, "NoahShell: pid is %d \n", pid);
  return 1;
}

// ppid function:
int ppid(char **args)
{
  pid_t ppid = getppid();
  fprintf(stderr, "NoahShell: ppid is %d \n", ppid);
  return 1;
}

// pwd function:
int pwd(char **args) {
  char *buf;
  buf=(char *)malloc(100*sizeof(char));
  getcwd(buf,100);
  fprintf(stderr, "%s \n", buf);
  return 1;
}

// For all non-builtin functions
int launch(char **args)
{
  pid_t pid, wpid;
  int status;
  
  int size = (sizeof(args) / sizeof(args[0]));

  pid = fork();
  pid_t childpid = getpid();

  // Error Forking
  if (pid < 0) {
    perror("NoahShell: error fork");
  } 

  // Child Process
  else if (pid == 0) {
  fprintf(stderr,"[%d] %s \n", childpid, args[0]);	// testing
    if (execvp(args[0], args) == -1) {
      perror("NoahShell: ");
    }
    exit(EXIT_FAILURE);
  }

  // Parent process WITHOUT WAIT (for &)
  else if (amper == 1) {
    fprintf(stderr,"[%d] ", pid);
    fprintf(stderr,"%s Exit %d\n", args[0], WEXITSTATUS(status));
    amper = 0;
  }
  
  // Parent process
  else {
    do {
      wpid = waitpid(pid, &status, WUNTRACED);
    } while (!WIFEXITED(status) && !WIFSIGNALED(status));
    fprintf(stderr,"[%d] ", pid);
    fprintf(stderr,"%s Exit %d\n", args[0], WEXITSTATUS(status));
  }

  return 1;
}


int execute(char **args)
{
  int i;

  if (args[0] == NULL) {
    // An empty command was entered.
    return 1;
  }

  else if (strcmp(args[0], "exit") == 0) {
    return (exitShell(args));
  }

  else if (strcmp(args[0], "cd") == 0) {
    return (cd(args));
  }

  else if (strcmp(args[0], "pid") == 0) {
    return (pid(args));
  }

  else if (strcmp(args[0], "ppid") == 0) {
    return (ppid(args));
  }

  else if (strcmp(args[0], "pwd") == 0) {
    return (pwd(args));
  }

  else {
    return launch(args);
  }
}

#define MAX_BUFSIZE 1024
char *read_line(void)
{
  int bufsize = MAX_BUFSIZE;
  int position = 0;
  char *buffer = malloc(sizeof(char) * bufsize);
  int c;

  if (!buffer) {
    fprintf(stderr, "NoahShell: allocation error\n");
    exit(EXIT_FAILURE);
  }

  while (1) {
    // Read a character
    c = getchar();

    // If we hit a &
    if (c == '&') {
      buffer[position] = '\0';
      amper = 1;
      return buffer;
    }
    
    // If we hit EOF, replace it with a null character and return.
    if (c == EOF || c == '\n') {
      buffer[position] = '\0';
      return buffer;
    } else {
      buffer[position] = c;
    }
    position++;

    // If we have exceeded the buffer, reallocate.
    if (position >= bufsize) {
      bufsize += MAX_BUFSIZE;
      buffer = realloc(buffer, bufsize);
      if (!buffer) {
        fprintf(stderr, "NoahShell: allocation error\n");
        exit(EXIT_FAILURE);
      }
    }
  }
}

#define TOKEN_BUFSIZE 64	// Max Token size
#define Delimiters " \t\r\n\a"	// Delimeters that constitute the end of a token

// Split text into tokens 
char **split_line(char *line)
{
  int bufsize = TOKEN_BUFSIZE, position = 0;
  char **tokens = malloc(bufsize * sizeof(char*));
  char *token;

  if (!tokens) {
    fprintf(stderr, "NoahShell: allocation error\n");
    exit(EXIT_FAILURE);
  }

  token = strtok(line, Delimiters);
  while (token != NULL) {
    tokens[position] = token;
    position++;

    if (position >= bufsize) {
      bufsize += TOKEN_BUFSIZE;
      tokens = realloc(tokens, bufsize * sizeof(char*));
      if (!tokens) {
        fprintf(stderr, "NoahShell: allocation error\n");
        exit(EXIT_FAILURE);
      }
    }

    token = strtok(NULL, Delimiters);
  }
  tokens[position] = NULL;
  return tokens;
}

void promptLoop(void)
{
  char *line;
  char **args;
  int status;

  do {
    printf("308sh> ");	// Prompt line
    line = read_line();
    args = split_line(line);
    status = execute(args);

    free(line);
    free(args);
  } while (status);
}

int main(int argc, char **argv)
{
  // Init text (for fun, my addition)
  init_shell();

  // Run loop for prompts
  promptLoop();

  return EXIT_SUCCESS;
}
