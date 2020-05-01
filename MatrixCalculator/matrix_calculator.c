#include <stdio.h>
#include <stdlib.h>
#include <string.h>



int main(int argc, char **argv) {
	printf("Matrix Row Operation Calculator v1.0 - Jason Tan\n");
	char *line = malloc(50);
	char *tok;
	int **matrix;
	int i, rows, cols;
	
	while(1) {
		printf("> ");
		i=0;
		fgets(line, 50, stdin);
		while(line[i++] != '\n');
		line[i-1] = '\0';

		if(strcmp(line, "")==0)
			continue;
		
		tok = strtok(line, " ");
		if(strcmp(tok, "quit")==0) {
			printf("Quitting..\n");
			break;
		}
		if(strcmp(line, "create")) {
			if(matrix != NULL) {
				printf("There is already a matrix active. Are you sure you want to enter a new matrix?\n[Y]es or [N]o?\n");
				char c;
				while(1) {
					c = getc(stdin);
					if(c == 'N' || c == 'n' || c == 'Y' || c == 'y')
						break;
					else
						printf("Please enter Y or N.\n");
				}
				if(c=='n' || c=='N')
					continue;
			}
			char *str = strtok(NULL, " ");
			rows = 
		}
		if(strcmp(line, "help") {
			printf("")
		}
		else {
			printf("Command not recognized\n");
		}
	}
	
	free(line);
	return 0;
}
