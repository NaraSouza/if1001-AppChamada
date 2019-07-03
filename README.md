# Projeto da disciplina IF1001 - Programação 3

#### Ideia 
Um aplicativo com duas visões - uma para professor e outra para aluno - em que é possível obter a presença do aluno em sala de aula através da proximidade entre os smartphones do aluno e do professor (utilizando Google Nearby Messages, comunicação unidirecional; é necessária conexão com a internet). O professor poderá exportar a lista de presença das disciplinas em uma planilha, caso deseje. O aluno terá acesso à sua lista de presença em aulas, além de informações como número de faltas e quantas faltas ainda serão toleradas dentro da carga horária da disciplina.

#### Público-alvo
Professores e alunos interessados em tornar mais ágil o processo de chamada em sala de aula. Além de poupar tempo de aula, a presença é confirmada sem a necessidade de uma ação direta do professor ou do aluno, utilizando informações sobre os dias e horários das aulas e dos alunos matriculados na disciplina.

#### Aplicativos similares
?

#### Estrutura da aplicação
 - Geral:
    - LoginActivity
    - RegisterActivity
 - Visão de professor - principais classes:
    - MainActivity (exibe disciplinas em que está cadastrado)
    - AddSubjectActivity (adicionar uma disciplina já existente)
    - CreateSubjectActivity (criar uma nova disciplina)
    - AddClassActivity (criar uma aula na disciplina em questão)
    - SubjectDetailActivity (Detalhes sobre a disciplina selecionada)
 - Para a visão de aluno:
    - AddSubjectActivity (adicionar uma turma/disciplina)
    - MainActivity (exibe disciplinas em que está cadastrado)
    - SubjectDetailActivity (Detalhes sobre a disciplina selecionada)
    
#### Sobre a implementação
A estrutura do aplicativo tem uma divisão simples entre pastas para "adapters", "models" e as activities.
O banco de dados foi implementado utilizando Firebase Realtime Database. Houveram alguns problemas com o primeiro projeto do Firebase criado, aparentemente relacionados com as regras do banco (apesar destas funcionarem corretamente ao realizar escritas e leituras no simulador do console). Um novo projeto foi criado e mantido até o momento sem regras para dedicar mais tempo à implementação do aplicativo. O app é baseado em duas visões: Professor e Aluno.
Como professor é possível cadastrar-se em uma disciplina já existente e também criar novas. Dentro de suas disciplinas, é capaz de criar novas aulas e visualizar relatórios sobre presença de alunos nas aulas, tanto individualmente quanto por comparação entre diferentes semestres (os relatórios não estão presentes na versão da master).
Como aluno é possível participar de turmas de disciplinas através de um código fornecido pelo professor. Dentro de cada disciplina é possível visualizar relatórios sobre o número de faltas até o momento e o número máximo de faltas permitido, de acordo com a carga horária da disciplina (os relatórios não estão presentes na versão da master). Também é possível confirmar presença em aulas através de um botão na tela da disciplina em questão. O gerenciamento da presença de alinos se dá da seguinte forma: utilizando a Google Nearby Messages API, é feita uma comunicação entre o app do aluno e o do professor. Na visão de professor, o aplicativo identifica se há uma aula naquele dia e horário (necessário abrir o app para isso) e, caso haja, executa um service (IntentService) que ficará escutando o canal em que as mensagens de confirmação de presença dos alunos serão enviadas. O service é executado até o final da duração da aula. Cada aluno pode então confirmar sua presença, através do envio de seu ID como usuário. O service recebe esse ID e atualiza o banco de dados com essa informação.

Alguns detalhes da interface de usuário e os templates dos relatórios citados acima encontram-se em outros branches (ver seção de divisão do trabalho). A comunicação entre os apps utilizando o Nearby Messages foi implementada apenas parcialmente.

#### Fluxo de navegação
[Ver mockup.](https://drive.google.com/open?id=1OlEZtHhe0wZf8swPdnnw77VngSgfGrF6)

#### Divisão do trabalho
Miriane: parte da interface de usuário, templates dos relatórios citados (branch basic-interface).  
Nara: Firebase, implementação geral da aplicação, teste da Nearby Messages API (branch testing-nearby-messages).
