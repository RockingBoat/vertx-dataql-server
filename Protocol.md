# Protocol

## Root

|Name|Type|IsNull|Description|
|----|----|------|-----------|
|q|Array<Query>|false| Все запросы здесь|
|o|options|true|Общие параметры для вызывов, например дебаг|

## Query

|Name|Type|IsNull|Description|
|----|----|------|-----------|
|service|String|false|Сервис куда мы собственно пойдем|
|version|String|false|Версия сервиса (может клеетить куда-то?)|
|method|String|false|Какую функу(метод) мы будем вызывать|
|fields|Array<String>| Какие поля мы хотим получить (подумать о регексах, вообщем чтобы можно было кастомно брать)|
|request|Array<Object>| true| Аргументы которые мы передадим функе|
|filter|TBA|TBA|TBA|
|filterFunction|TBA|TBA|TBA|
|sub|Array<Query>|true| подзапросы|
|filter|Extract|true|TBA|

## Extract

|Name|Type|IsNull|Description|
|----|----|------|-----------|
|


## Server
### Root
#### query (q)
Список запросов к сервисам
Тип List<Query>

#### options (o)
Список опций для данного запроса
Тип options?

#### request (r)
Одно тело для всех рутовых запросов
Тип Object?

### Query
#### service (s)
Название сервиса,тип String

#### version (v)
Версия сервиса,тип String

#### method (m)
Метод сервиса,тип String

