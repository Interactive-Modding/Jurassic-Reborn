#include <iostream>
#include <vector>
#include <fstream>

//this code was stolen almost verbatim from the entity type one... just tweaked the base string. Worked like a treat. Saved a whole lot of time.
std::string splitName(std::string input);

std::string toCaps(std::string &input){
    std::string out;
    for(auto c : input){
        out += toupper((int)c);
    }
    return out;
}


//~ means regular name
//` means split name, i. e. DiplocaulusEntity -> Diplocaulus
//*C means * in caps
const std::string base = "//~ registration: auto generated\n"
                         "DinosaurRenderInfo `Info = new DinosaurRenderInfo(`C, new `Animator());\n"
                         "event.registerEntityRenderer(ModEntities.`C_ENTITY_TYPE.get(), (ctx) ->\n"
                         "                new DinosaurRenderer(ctx,\n"
                         "                        getDefaultModelFromDinosaur(`Info),\n"
                         "                        0.5f,\n"
                         "                        `Info));\n";

std::string mainFunc(std::string &str){
    std::string output = base;
//    copy(str.begin(), str.end(), output.begin());


    const std::string& atila = str;
    std::string jankQuote = splitName(str);
    if(str.find("Entity") != std::string::npos){
        while(output.find('~') != std::string::npos) {
            output.replace(output.find('~'), 1, atila);
        }
        while(output.find('`') != std::string::npos) {

            output.replace((output.find('`')), (output[(output.find('`')+1)] == 'C') ? 2 : 1, (output[(output.find('`')+1)] == 'C') ? toCaps(jankQuote) : jankQuote );
        }


    }


    return output;
}

int main() {
//    cout << "Hello, World!" << endl;
    while(true) {
        std::cout << "Please enter an entity name or specify a path to a file with a list of them:" << std::endl;
        std::string input;
        std::getline(std::cin, input);


        if (input == "exit") {
            break;
        } else if (input[0] == '/') {
            if(input[0] == '/'){
                //do file input stuff
                std::ifstream fileInput;
                std::ofstream fileOutput;

                std::vector<std::string> fInput;
                fileInput.open(input);
                while(true){
                    std::string temp;
                    getline(fileInput, temp);
                    if(fileInput.fail())
                        break;
                    fInput.push_back(temp);
                }
                fileInput.close();
                std::vector<std::string> outputs;
                for(std::string& str : fInput){
                    std::string temp = mainFunc(str);
                    outputs.push_back(temp);
                    std::cout << temp << std::endl;
                }

            }
        }else{
            std::cout << mainFunc(input) << std::endl;
        }
    }
    return 0;
}

std::string splitName(std::string input){
    std::string output;

    for (int i = 1; i < input.length(); ++i) {
        if(!islower(input[i])){
            output = input.substr(0, i);
        }
    }



    return output;
}
