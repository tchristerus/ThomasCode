# ThomasCode
In order to learn the working of scripting languages and interpreters I decided to build it myself. Atleast giving it a try.

This language is only created for learning purposes and should not be used to build stuff.

## Some example pieces
Keep in mind that this is just some of my ideas thrown together in a codeblock :P
```python

call showMessage("Thomas");
  
func showMessage(<message>){
    call prepareMessage(<message>);
}
  
rfunc prepareMessage(<message>){
    ret "Hi " + <message>;
}
  
# Not implemented yet (or half)
class <Greeter> {
    priv <defaultPrefix> = "hello";
  
    pub func showMessage(<message>){
        print("hi" + <message>);
    }
  
    priv rfunc prepareMessage(<message>){
        ret <defaultPrefix> + <message>;
    }
}
```
