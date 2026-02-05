#include <iostream>
#include <fstream>
#include <vector>
#define string std::string

string toLowerCase(string &input){
    string out;
    for(char c : input){
        out += tolower(c);
    }
    return out;
}

string mainFunc(string input, string properties = ""){
    int a = 0;
    for (int i = 0; i < 4; ++i) {
        if(input[i] == ' ')
            a++;
    }
    input.replace(0, a, "");

    string input3 = properties;


//        std::cout << input.find("final ") << std::endl;

    string input2 = input;

//        std::copy(input.begin(), input.end(), input2.begin());
    input2.replace(input2.find("final "), 6, "");


    string CLass = input2.substr(14,  input2.find(" ", 14)-14);
    input2.replace(14, input2.find(" ", 15)-14, "RegistryObject<" + CLass + ">");


    int start = input2.find(CLass) + CLass.length()+1;
    string thing = input2.substr(start+1, input2.find(" ", start+1)-(start+1));
    input2.insert(input2.find("= ")+2, "modBlocks.register(\"" + toLowerCase(thing) + "\", () -> " );

    if(input2.find("BasicBlock") != string::npos)
        input2.replace(input2.find("BasicBlock"), 5, "");
    if(input2.find("Material.ROCK") != string::npos)
        input2.replace(input2.find("Material.ROCK")+9, 4, "STONE");
    if(input2.find("setHardness") != string::npos)
        input2.replace(input2.find("setHardness"), 11, "setStrength");

    input2.insert(input2.find_last_of("()"), input3);

    input2.insert(input2.length()-2, ")");


    return input2;
}

int main() {


    while(true){
        std::cout << "Please enter a new 1.12 block code or specify a path to a file:" << std::endl;
        string input;
        std::getline(std::cin, input);


        if(input == "exit"){
            break;
        }

        else if(input == ""){
            continue;
        }
        if(input[0] == '/'){
            //do file input stuff
            std::ifstream fileInput;
            std::ofstream fileOutput;

            std::vector<string> fInput;
            fileInput.open(input);
            while(true){
                string temp;
                std::getline(fileInput, temp);
                if(fileInput.fail())
                    break;
                fInput.push_back(temp);
            }
            fileInput.close();
            std::vector<string> outputs;
            for(string& str : fInput){
                string temp = mainFunc(str);
                outputs.push_back(temp);
                std::cout << temp << std::endl;
            }

        }else{
            std::cout << "please enter block properties" << std::endl;
            string input3;
            std::getline(std::cin, input3);
            std::cout << mainFunc(input, input3) << std::endl;
        }


        std::cin.clear();




    }
    return 0;
}
