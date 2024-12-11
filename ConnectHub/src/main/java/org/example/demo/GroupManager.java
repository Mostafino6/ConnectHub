package org.example.demo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileWriter;

public class GroupManager {
    private static final String DATABASE_FILE = "C:\\Users\\Gebriel\\Desktop\\Term 5\\Programming II\\Lab9\\ConnectHub\\ConnectHub\\src\\main\\java\\org\\example\\demo\\groups.json";
    private static final DatabaseManager databaseManager = new DatabaseManager();
    private ArrayList<User> users = databaseManager.readUsers();

    public GroupManager() throws Exception {
    }

    public ArrayList<Group> readGroups() throws Exception {
        File file = new File(DATABASE_FILE);
        if (!file.exists()) {
            return new ArrayList<>(); // Return an empty list if the file does not exist
        }

        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(file));

        ArrayList<Group> groupList = new ArrayList<>();
        HashMap<String, Group> groupMap = new HashMap<>();

        // First pass: Create Group objects
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;

            String groupID = (String) jsonObject.get("groupID");
            String creator = (String) jsonObject.get("creator");
            String groupName = (String) jsonObject.get("groupName");
            String groupDescription = (String) jsonObject.get("groupDescription");
            String groupIcon = (String) jsonObject.get("groupIcon");
            User owner = getUserfromUserID(creator);

            Group group = new Group();
            group.setGroupID(groupID);
            group.setCreator(owner);
            group.setGroupName(groupName);
            group.setGroupDescription(groupDescription);
            group.setGroupIcon(groupIcon);

            groupList.add(group);
            groupMap.put(groupID, group);
        }

        // Second pass: Populate hierarchy
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;

            String groupID = (String) jsonObject.get("groupID");
            Group group = groupMap.get(groupID);

            JSONObject hierarchyObject = (JSONObject) jsonObject.get("hierarchy");

            JSONArray adminsArray = (JSONArray) hierarchyObject.get("admins");
            group.getHierarchy().setAdmins(getUserIDsFromJSON(adminsArray));

            JSONArray membersArray = (JSONArray) hierarchyObject.get("members");
            group.getHierarchy().setMembers(getUserIDsFromJSON(membersArray));

            JSONArray requestsArray = (JSONArray) hierarchyObject.get("requests");
            group.getHierarchy().setRequests(getUserIDsFromJSON(requestsArray));
        }

        return groupList;
    }

    public void writeGroup(Group group) throws Exception {
        ArrayList<Group> groupList = readGroups();
        if (groupList == null) {
            groupList = new ArrayList<>();
        }

        // If it's a new group, generate a unique groupID
        if (group.getGroupID() == null || group.getGroupID().isEmpty()) {
            String newGroupID = getNextGroupID(groupList);
            group.setGroupID(newGroupID);
        }

        // Update existing group or add a new group
        boolean groupExists = false;
        for (int i = 0; i < groupList.size(); i++) {
            if (groupList.get(i).getGroupID().equals(group.getGroupID())) {
                groupList.set(i, group);
                groupExists = true;
                break;
            }
        }

        if (!groupExists) {
            groupList.add(group);
        }

        // Prepare the JSON array
        JSONArray jsonArray = new JSONArray();
        for (Group existingGroup : groupList) {
            JSONObject jsonGroup = new JSONObject();

            jsonGroup.put("groupID", existingGroup.getGroupID());
            jsonGroup.put("creator", existingGroup.getCreator().getUserID()); // Store only the userID
            jsonGroup.put("groupName", existingGroup.getGroupName());
            jsonGroup.put("groupDescription", existingGroup.getGroupDescription());

            String icon = existingGroup.getGroupIcon();
            if (icon != null && icon.startsWith("file:///")) {
                icon = icon.substring(8); // Strip "file:///" prefix
            }
            if (icon != null) {
                icon = icon.replace("\\", "/"); // Normalize the path
            }
            jsonGroup.put("groupIcon", icon);

            // Hierarchy information
            JSONObject hierarchyObject = new JSONObject();
            hierarchyObject.put("admins", getJSONFromUserIDs(existingGroup.getHierarchy().getAdmins()));
            hierarchyObject.put("members", getJSONFromUserIDs(existingGroup.getHierarchy().getMembers()));
            hierarchyObject.put("requests", getJSONFromUserIDs(existingGroup.getHierarchy().getRequests()));
            jsonGroup.put("hierarchy", hierarchyObject);

            jsonArray.add(jsonGroup);
        }

        // Write the updated JSON to the file
        try (FileWriter writer = new FileWriter(DATABASE_FILE)) {
            writer.write("[\n");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonGroup = (JSONObject) jsonArray.get(i);
                writer.write("  {\n");

                // Write group details with new lines for readability
                writer.write("    \"groupID\": \"" + jsonGroup.get("groupID") + "\",\n");
                writer.write("    \"creator\": \"" + jsonGroup.get("creator") + "\",\n");
                writer.write("    \"groupName\": \"" + jsonGroup.get("groupName") + "\",\n");
                writer.write("    \"groupDescription\": \"" + jsonGroup.get("groupDescription") + "\",\n");
                writer.write("    \"groupIcon\": \"" + jsonGroup.get("groupIcon") + "\",\n");

                // Write hierarchy object (admins, members, requests)
                JSONObject hierarchyObject = (JSONObject) jsonGroup.get("hierarchy");
                writer.write("    \"hierarchy\": {\n");

                // Write admins
                JSONArray adminsArray = (JSONArray) hierarchyObject.get("admins");
                writer.write("      \"admins\": " + adminsArray.toString() + ",\n");

                // Write members
                JSONArray membersArray = (JSONArray) hierarchyObject.get("members");
                writer.write("      \"members\": " + membersArray.toString() + ",\n");

                // Write requests
                JSONArray requestsArray = (JSONArray) hierarchyObject.get("requests");
                writer.write("      \"requests\": " + requestsArray.toString() + "\n");

                writer.write("    }\n");

                // Close the group object
                if (i < jsonArray.size() - 1) {
                    writer.write("  },\n");
                } else {
                    writer.write("  }\n");
                }
            }
            writer.write("]");
        }
    }
    private User getUserfromUserID(String userID) {
        for (User user : users) {
            if (user.getUserID().equals(userID)) {
                return user;
            }
        }
        return null;
    }
    private ArrayList<User> getUserIDsFromJSON(JSONArray jsonArray) {
        ArrayList<User> users = new ArrayList<>();
        User user;
        for (Object idObj : jsonArray) {
            user = getUserfromUserID((String) idObj);
            users.add(user);
        }
        return users;
    }

    private JSONArray getJSONFromUserIDs(ArrayList<User> users) {
        JSONArray jsonArray = new JSONArray();
        for (User user : users) {
            jsonArray.add(user.getUserID());  // Add only the userID instead of the whole User object
        }
        return jsonArray;
    }

    public String getNextGroupID(ArrayList<Group> groupList) {
        int maxID = 0;
        for (Group group : groupList) {
            try {
                int groupID = Integer.parseInt(group.getGroupID());
                maxID = Math.max(maxID, groupID);
            } catch (NumberFormatException e) {
                continue;
            }
        }
        return String.valueOf(maxID + 1);
    }
}
