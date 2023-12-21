API_URL="http://localhost:8080/api/v1"
export API_URL

random_string=""
AUTH_TOKEN_ALISA=""
AUTH_TOKEN_BOB=""
AUTH_TOKEN_CAROL=""

#set -x
source endpoints.sh
source scenario.sh

echo -e "$random_string \n $AUTH_TOKEN_ALISA \n $AUTH_TOKEN_BOB \n $AUTH_TOKEN_CAROL" > output.txt

echo -e "\n ======End Of Sources======"

response=$(create_directory $AUTH_TOKEN_ALISA alisa)
echo $response