workflow "Resolve Dependencies" {
  on = "push"
  resolves = ["Build"]
}

action "Resolve" {
  uses = "LucaFeger/action-maven-cli@aed8a1fd96b459b9a0be4b42a5863843cc70724e"
  runs = "mvn dependency:resolve"
}

action "Build" {
  uses = "LucaFeger/action-maven-cli@aed8a1fd96b459b9a0be4b42a5863843cc70724e"
  needs = ["Resolve"]
  runs = "mvn clean install"
  args = "skipTests=true"
}
