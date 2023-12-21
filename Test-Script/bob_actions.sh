echo -e "\n ========Registration Bob========"
echo Response $(registration "Bob" "Bobov" "$random_string@b.ru" "bob")
sleep 1;

echo -e "\n ========Authorisation Bob========"
response=$(authorization "$random_string@b.ru" "bob")
echo Response $response

AUTH_TOKEN_BOB=$(echo "${response::-4}" | jq -r '.token')
sleep 1;

echo -e "\n ========Get Secret By Token For Bob========"
response=$(create_secret_by_token $AUTH_TOKEN_BOB $share_token)
echo Response "${response::-4}"
echo Response code: "$(echo "$response" | tail -c 4)"
sleep 1;