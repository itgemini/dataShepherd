/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper.writer.style.condional;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Registry implements Command {
    private final Set<Command> onComplete = ConcurrentHashMap.newKeySet();
    private final Set<Command> before = ConcurrentHashMap.newKeySet();
    private final Set<Command> onNext = ConcurrentHashMap.newKeySet();

    public void onNext(Command item) {
        onNext.add(item);
    }
    public void onBefore(Command item) {
        before.add(item);
    }

    public void onComplete(Command item) {
        onComplete.add(item);
    }

    public void clear() {
        before.clear();
        onNext.clear();
        onComplete.clear();
    }

    @Override
    public void execute() {
        before.forEach(Command::execute);
        onNext.forEach(Command::execute);
        onComplete.forEach(Command::execute);
    }
}