#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char **argv) {
	printf("Matrix Row Operation Calculator v1.0 - Jason Tan\n");
	char *choice = malloc(50);
	char *tok;
	
	while(1) {
		printf("> ");
		fgets(choice, 50, stdin);
		
		tok = strtok(choice, " ");
		if(strcmp(tok, "quit")==0) {
			printf("Quitting..\n");
			break;
		}
		else {
			printf("Command not recognized\m");
		}
	}
	
	free(choice);
	return 0;
}
