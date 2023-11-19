# Context

## ApplicationContext

어플리케이션의 수명 동안 계속 유지되어야 하는 무언가를 다룰 때 사용

## Context 

현재 Actiivty와 관련된 작업을 수행할 때 사용

### tip
- 싱글톤으로 사용하려고 하는 경우 memory leak 이 일어날 수 있음.
- Application Context 는 테마 같은 것을 신경쓰지 않는다.


<code-block lang="kotlin">
class MyActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            // Right context for creating views
            val view = View(this)
        }
}
</code-block>