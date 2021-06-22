Nome: Lucas Sommer

Projeto final GB computação grafica
Projeto utiliza dois do mesmo modelo, um tem apenas 50% da malha e o outro apenas 20% da malha

Projeto carrega ambos os modelos, calcula em ms o tempo para carregar cada um separado
e calcula(Para GPUs da Nvidia) a memória disponível no momento de carregar o primeiro e no momento de carregar o segundo
não é um cálculo exato, mas serve como informação extra de como está a memoria da placa.

COMANDOS:
//x
numpad 4 -> rotaciona em um angulo negativo o eixo x;
numpad 6 -> rotaciona em um angulo positivo o eixo x;

//y
numpad 8 -> rotaciona em um angulo negativo o eixo y;
numpad 2 -> rotaciona em um angulo positivo o eixo y;

//z
numpad 9 -> rotaciona em um angulo negativo o eixo z;
numpad 1 -> rotaciona em um angulo positivo o eixo z;

Seta para a esquerda -> rotaciona em um angulo positivo os 3 eixos;
Seta para a direita -> rotaciona em um angulo negativo os 3 eixos;


tempo para visualizar modelo com 50% -> 9-17ms =~
tempo para visualizar modelo com 20% -> 9-21ms =~

O tempo para carregar o modelo para dentro do projeto está no console ao executar o programa.