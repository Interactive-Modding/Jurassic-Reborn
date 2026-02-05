#include <iostream>
#include <vector>
#include <fstream>

std::string splitName(std::string input);

std::string toCaps(std::string input);


//~ means regular name
//` means split name, i. e. DiplocaulusEntity -> Diplocaulus
//*C means all caps
//@ means EntityType<~>
const std::string base = "public static DeferredRegister<EntityType<~>> `C_ENTITY_TYPE = MOD_ENTITY_TYPES.<EntityType<~>>register(\"`\".toLowerCase(Locale.ROOT), () -> EntityType.Builder.<~>of(((type, world) -> new ~(world, type)), MobCategory.CREATURE).build(null));";

std::string mainFunc(std::string &str);

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

std::string mainFunc(std::string &str){
    std::string output = base;
//    copy(str.begin(), str.end(), output.begin());


    std::string atila = str;
    std::string jankQuote = splitName(str);
    std::string at = "EntityType<" + atila + ">";
    if(str.find("Entity") != std::string::npos){
        for (int i = 0; i < 4; ++i) {
            output.replace(output.find('~'), 1, atila);
        }
        for (int i = 0; i < 2; ++i) {

            output.replace((output.find('`')), (output[(output.find('`')+1)] == 'C') ? 2 : 1, (output[(output.find('`')+1)] == 'C') ? toCaps(jankQuote) : jankQuote );
        }


    }


    return output;
}

std::string toCaps(std::string input){
    std::string out;
    for(char c : input){
        out += toupper(c);
    }
    return out;
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