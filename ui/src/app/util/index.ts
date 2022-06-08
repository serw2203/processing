export const wait = (timeout: Number) => {
    let counter = 0, start = new Date().getTime(), end = 0;
    while (counter < timeout) {
        end = new Date().getTime();
        counter = end - start;
    }
};
