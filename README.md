# ToDoList-Dio
>  Esse projeto foi proposto no Bootcamping Santader Mobile Developer. Ele é um projeto de lista de lembranças e tarefas. O projeto utiliza os seguintes items: injeção de dependência com Koin & Dagger Hilt, banco de dados com Room, e Navigation.

#### Branches

Esse projeto está dividido em duas branches:

- [ToDoList-Dio com Koin](https://github.com/marceloalves95/ToDoList-Dio/tree/ToDoListDio-Koin)
- [ToDoList-Dio com DaggerHilt](https://github.com/marceloalves95/ToDoList-Dio/tree/ToDoListDio-DaggerHilt)

Para mais detalhes sobre a implementação de cada injeção de dependência, escolha a branch acima ou digite o código abaixo para baixar a branch especifica:

```
//Branch ToDoListDio-Koin
git clone https://github.com/marceloalves95/ToDoList-Dio.git --branch ToDoListDio-Koin
//Branch ToDoListDio-DaggerHilter
git clone https://github.com/marceloalves95/ToDoList-Dio.git --branch ToDoListDio-DaggerHilt
```

#### Instalação com o Koin

##### Dependências

Para o uso do Navigation, Room e o Koin, inclua o seguinte código no `build.gradle` do projeto e atualize o `Gradle`:

```groovy
buildscript {

    ext{
        //Room Version
        room_version="2.3.0"
        //Koin Version
        koin_version="3.1.1"
        ........
    }
    .......
}
```

Inclua as seguintes dependências adicionando o código no `build.gradle` do módulo do projeto e atualize o `Gradle`:

```groovy
plugins {
    //Navigation Safe Args
    id "androidx.navigation.safeargs.kotlin"
    ........
}
dependencies {
    //Navigation
    implementation "androidx.navigation:navigation-fragment-ktx:$navigation_version"
    implementation "androidx.navigation:navigation-ui-ktx:$navigation_version"
    //Room
    kapt "androidx.room:room-compiler:$room_version"
    implementation "androidx.room:room-runtime:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    //Koin
    implementation "io.insert-koin:koin-android:$koin_version"
    testImplementation "io.insert-koin:koin-test:$koin_version"
    ..........
}
```

##### Aplicação

Crie uma classe chamado `Application`:

```kotlin
class Application:Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            modules(appModules)
        }

    }
}
```

##### AndroidManifest

Acrescente na linha no `AndroidManifest.xml`

```xml
<application
        android:name=".di.Application"
        //Seu tema
        android:theme="@style/Theme.MeuTema">
       .....
</application>
```

##### Módulo

Crie um arquivo `AppModules.kt`:

```kotlin
val appModules = module {
    single {
        Room.databaseBuilder(
            get(),
            //Aqui você define a classe do seu banco de dados
            AppDatabase::class.java,
            //Nome do seu banco
            BD_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }
    single{
        //Módulo da interface do banco
        get<AppDatabase>().tarefaDao()
    }
    single {
        //Repository
        TarefaRepository(get())
    }
    viewModel {
        //View Model
        TarefaViewModel(get())
    }

}
```

##### Tabelas

Crie a tabela do seu banco:

```kotlin
@Entity(tableName = "tarefa")
data class Tarefa(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val titulo: String,
    val descricao: String,
    val horario: String,
    val data: String,
    val cor: String,
    val nomeCor: String,
    val status:String,
    var selected: Boolean
)
```

##### DAO

Crie uma interface para o `Dao` do projeto:

```kotlin
@Dao
interface TarefaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tarefa: Tarefa)
    @Update
    suspend fun update(tarefa: Tarefa)
    @Query("SELECT * FROM tarefa")
    suspend fun allTarefas():MutableList<Tarefa>
    @Query("DELETE FROM tarefa WHERE id IN (:id)")
    suspend fun deleteAll(id:MutableList<Int>)
}
```

##### Repository

Crie uma classe `Repository`

```kotlin
class TarefaRepository(private val tarefaDao: TarefaDao) {

    suspend fun insert(tarefa: Tarefa) = tarefaDao.insert(tarefa)
    suspend fun update(tarefa: Tarefa) = tarefaDao.update(tarefa)
    suspend fun allTarefas():MutableList<Tarefa> = tarefaDao.allTarefas()
    suspend fun deleteAll(id:MutableList<Int>) = tarefaDao.deleteAll(id)
    
}
```

##### ViewModel

Crie a `ViewModel` do seu projeto:

```kotlin
class TarefaViewModel(private val repository: TarefaRepository):ViewModel(){

    val listAll = MutableLiveData<MutableList<Tarefa>>()

    fun adicionarTarefas(tarefa: Tarefa){
        viewModelScope.launch {
            repository.insert(tarefa)
        }
    }
    fun atualizarTarefas(tarefa: Tarefa){
        viewModelScope.launch {
            repository.update(tarefa)
        }
    }
    fun listarTarefas(){
        viewModelScope.launch {
            listAll.value = repository.allTarefas()
        }
    }
    fun deleteAll(id:MutableList<Int>){
        viewModelScope.launch {
            repository.deleteAll(id)
        }
    }
}
```
