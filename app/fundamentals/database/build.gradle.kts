plugins {
    id("li.raphael.kokus.module")
    id("li.raphael.kokus.database")
}

c4Container {
    description = "Database infrastructure for all modules"
    tags.addAll("Database")
}
