echo -e "\n ========Registration Alisa========"
random_string=$(head /dev/urandom | tr -dc 'a-zA-Z0-9' | head -c 4)
echo Response $(registration "Alisa" "Alisovna" "$random_string@a.ru" "alisa")
sleep 1;

echo -e "\n ========Authorisation Alisa========"
response=$(authorization "$random_string@a.ru" "alisa")
echo Response $response

AUTH_TOKEN_ALISA=$(echo "${response::-4}" | jq -r '.token')
sleep 1;

echo -e "\n ========Create Secrets========"
response=$(create_secret $AUTH_TOKEN_ALISA secret1 secret1 secret1 secret1)
echo Response $response
sleep 1;

response=$(create_secret $AUTH_TOKEN_ALISA secret2 secret2 secret2 secret2)
echo "Response $response"
sleep 1;

response=$(create_secret $AUTH_TOKEN_ALISA secret3 secret3 secret3 secret3)
echo Response $response
sleep 1;

echo -e "\n ========Get all Secrets========"
response=$(get_all $AUTH_TOKEN_ALISA)
echo Response $response

secret_id=$(echo "${response::-4}" | jq -r '.secrets[0].id')
sleep 1;

echo -e "\n ========Get One Secret========"
response=$(get_one $AUTH_TOKEN_ALISA $secret_id)
echo Response $response
sleep 1;

echo -e "\n ========Create Share Secret Token========"
response=$(get_share_token $AUTH_TOKEN_ALISA $secret_id)
echo Response $response

share_token=$(echo "${response::-4}" | jq -r '.id')
sleep 1;