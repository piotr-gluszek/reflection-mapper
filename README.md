# Reflection Mapper

## How does it work
1. Object is taken as the argument
2. Every field of the object is written to array
3. Object constructor is taken to create new instance
4. Object fields are read and stored in the array
5. Array is iterated over:
  -if field name is present in mapping file its map is read and applied
  -if field is absent or its value has no mapping its rewritten
6. Mapped object is returned

## Requirements
1. Mapped object has to have default constructor
2. Proper mapping file has to be prepared

### Sample mapping file content
```json
{
  "status": {
    "202": "ACCPETED",
    "400": "BAD REQUEST"
  },
  "message": {
    "Fatal error": "Quite bad situation",
    "Ok": "Excellent job!"
  }
}
```
> Object with field "status" will have its value changed to: "ACCEPTED" if its current value is "202" and so on.

## Dependencies
1. jackson-databind 2.7.3
2. jackson-core 2.7.3
3. jackson-annotation 2.7.3
