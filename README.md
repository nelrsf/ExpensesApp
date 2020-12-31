# ExpensesApp
UNIVERSIDAD AUTÓNOMA DE BUCARAMANGA

Documento de Diseño Aplicación Móvil

Desarrollo de aplicaciones para dispositivos móviles
Tecnologías Avanzadas para el Desarrollo de Software

Presentado por:

Nelson Reinaldo Santamaría Forero
Bucaramanga
2020


1.	Introducción

1.1.	Propósito del sistema.

La aplicación consiste en una herramienta móvil de control de gastos diarios tales como comida, hospedaje, transporte, etc. para profesionales que requieran llevar un control de sus gastos en campo, esta debe ser administrable para diferentes tipos de proyectos en una empresa.

Muchas veces en la cotidianidad del ejercicio de la Ingenieria los profesionales se ven inmersos en unas series de tareas tediosas que le consumen toda la jornada laboral y no les deja tiempo para administrar los gastos básicos de la persona.

El objetivo primordial de esta aplicación es que el usuario simplemente con abrir la App, llenar un formulario simple, sencillo, amigable e intuitivo pueda almacenar todos sus gastos en una base de datos en la cual posteriormente pueda consultar todos sus gastos clasificados en un determinado periodo de tiempo.

1.2. Objetivos del diseño

La aplicación consiste en un formulario en el cual el usuario ingresará dos datos obligatorios los cuales serán; el monto del gasto, una clasificación de este (alimentación, transporte, hospedaje, etc.) y el nombre del proyecto o la empresa a quien desee cargarle esos gastos.

La app capturará otros datos automáticamente tales como la ubicación y la fecha para que el usuario no tenga que ingresar tanta información.

Y adicionalmente el usuario podrá cargar una información opcional al formulario, por ejemplo; comentarios u observaciones y una foto de la factura en cuestión.

Toda la información debe ser cargada en una base de datos de modo que el usuario puede solicitar los gastos clasificados por alimentación, hospedaje, transporte y tipo de proyecto en un rango de fechas que sean seleccionadas por el usuario.


1.3. Definiciones, acrónimos y abreviaturas

PROYECTO, PROJECT: Conjunto de gastos que hacen referencia al mismo trabajo u objetivo.

USUARIO, USER: Persona que utiliza la aplicación Expenses App, alimenta y consulta la base de datos con diferentes proyectos y gastos.

2.	Representación de la arquitectura.

La aplicación implementará una arquitectura de tipo MVVM (Model – View – View Model) ya que está es adecuada para aplicaciones móviles por su separación entre la lógica de negocio y las vistas además de abstraer el enlazamiento (binding) entre la vista y el modelo de la vista, adicionalmente el modelo de la vista permite mantener la información durante el ciclo de vida de las vistas o actividades.

3.3	 Herramientas de Desarrollo e implementación

A continuación, se presentan las herramientas utilizadas para el desarrollo de esta aplicación:
IDE (Integrated Development Enviroment):	Android Studio
Lenguaje de programación.	JAVA 8
Otros frameworks:	Android Jetpack (Se utilizará para el enlace de información Binding)
Cloud Firestore (Almacenamiento en base de datos)


