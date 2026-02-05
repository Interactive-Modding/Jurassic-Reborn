#include <iostream>
#include <vector>
#include <fstream>
#include <sstream>
#include <map>


//this code was stolen almost verbatim from the entity type one... just tweaked the base string. Worked like a treat. Saved a whole lot of time.
std::string splitName(std::string input);



std::string handleInput(std::string input);

std::string toCaps(std::string &input){
    std::string out;
    for(auto c : input){
        out += toupper((int)c);
    }
    return out;
}


//~ means regular name
//` means split name, i. e. DiplocaulusEntity -> Diplocaulus
//the suffix -C tells the program to capatalize what comes before.
const std::string base = "this.dinosaur = DinosaurHandler.`C;";

const std::string outputDir = "/Users/gammaposselt/Documents/c++ automation io/output-dinoadder/";

//this function takes a string that contains a complete file.
std::string mainFunc(std::string &str){
    std::string output = base;
//    copy(str.begin(), str.end(), output.begin());



    const std::string refrenceString = "public class ";
    const int refrencepos = str.find(refrenceString);

    const std::string& atila = str.substr(refrencepos + refrenceString.length(), str.find(" extends ") - (refrencepos + refrenceString.length()));
    std::string jankQuote = splitName(atila);
    if(atila.find("Entity") != std::string::npos){
        while(output.find('~') != std::string::npos) {
            output.replace(output.find('~'), 1, atila);
        }
        while(output.find('`') != std::string::npos) {

            output.replace((output.find('`')), (output[(output.find('`')+1)] == 'C') ? 2 : 1, (output[(output.find('`')+1)] == 'C') ? toCaps(jankQuote) : jankQuote );
        }



    }

    const std::string superstring = "super(world, type);";
    const int superpos = str.find(superstring);
    str.insert(superpos + superstring.length(), "\n\t\t" + output);

    std::cout << str << std::endl;



    return str;
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
            handleInput(input);
        }else if(input.find("DIRECTORY: ") == 0){
            input = input.substr(((std::string)"DIRECTORY: ").length());

            std::map<std::string, const std::filesystem::directory_entry> inputfilemap;

            for(auto& entry : std::filesystem::directory_iterator(input)){
                int namesplit = entry.path().string().find_last_of('/');
                std::string filename = entry.path().string().substr(namesplit);
                std::cout << filename << std::endl;
                inputfilemap.insert(std::pair<std::string, const std::filesystem::directory_entry>(filename, entry));

            }

            std::map<std::string, std::string> outputfilemap;

            for(auto& filePair : inputfilemap){
                std::string processedFile = handleInput(filePair.second.path().string());
                outputfilemap.insert(std::pair<std::string, std::string>(filePair.first, processedFile));
            }

            std::ofstream fileOutput;

            for(auto& filePair : outputfilemap){
                fileOutput.open(outputDir + filePair.first, std::fstream::trunc | std::fstream::out);
                fileOutput << filePair.second << std::endl;
                fileOutput.close();
            }

        }

    }
    return 0;
}

std::string handleInput(std::string input){
    if(input[0] == '/'){
        //do file input stuff
        std::ifstream fileInput;

//        std::vector<std::string> fInput;
        std::stringstream inputStrFileStream;
        fileInput.open(input);
        while(true){
            std::string temp;
            getline(fileInput, temp);
            if(fileInput.fail())
                break;
//                    fInput.push_back(temp);
            inputStrFileStream << temp << std::endl;
        }
        fileInput.close();
//        std::vector<std::string> outputs;
//        for(std::string& str : fInput){
//            std::string temp = mainFunc(str);
//            outputs.push_back(temp);
//            std::cout << temp << std::endl;
//        }
        std::string inputFile = inputStrFileStream.str();
//        std::cout << mainFunc(inputFile) << std::endl;
        return mainFunc(inputFile);
    }
    return "";
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
