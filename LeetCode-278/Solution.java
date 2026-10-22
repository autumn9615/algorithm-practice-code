// ============================================================================
// 力扣 278. 第一个错误的版本
// ----------------------------------------------------------------------------
// 题目描述
// 你是产品经理，目前正在带领一个团队开发新的产品。不幸的是，你的产品的最新
// 版本没有通过质量检测。由于每个版本都是基于之前的版本开发的，所以错误的
// 版本之后的所有版本都是错的。
//
// 假设你有 n 个版本 [1, 2, ..., n]，你想找出导致之后所有版本出错的第一个
// 错误的版本。
//
// 你可以通过调用 bool isBadVersion(version) 接口来判断版本号 version 是否
// 在单元测试中出错。实现一个函数来查找第一个错误的版本。你应该尽量减少对
// 调用 API 的次数。
//
// 函数签名（力扣已经给出，不用自己读输入，把方法补全提交即可）
//   public class Solution extends VersionControl
//   public int firstBadVersion(int n)
//   isBadVersion(version) 是父类 VersionControl 里已经实现好的接口，直接
//   调用即可：返回 true 表示这个版本是坏的，false 表示是好的。
//   （本地想跑要自己补一个带 isBadVersion 的 VersionControl 类；力扣提交时
//   不用带，平台会提供。）
//
// 输入 #1
//   n = 5, bad = 4
// 输出 #1
//   4
//   解释：调用 isBadVersion(3) -> false
//         调用 isBadVersion(5) -> true
//         调用 isBadVersion(4) -> true
//         所以，4 是第一个错误的版本。
//
// 输入 #2
//   n = 1, bad = 1
// 输出 #2
//   1
//
// 提示
//   1 <= bad <= n <= 2^31 - 1
//
// 说明/提示
// 【样例说明】bad 是后台设定的"第一个坏版本"，不是方法参数；方法只拿得到
//   n，其余信息全靠 isBadVersion 去问。例 1 里版本 1~3 是好的、4~5 是坏的，
//   问出来第一个坏的是 4；例 2 只有一个版本且它是坏的，直接就是 1。
//   按下面的二分实际走一遍例 1（bad = 4）：
//     left=1, right=5 → mid=3，isBadVersion(3)=false → left=4
//     left=4, right=5 → mid=4，isBadVersion(4)=true  → right=4
//     left==right==4，退出循环，返回 4，和样例一致（只调了 2 次接口）。
// 【评测用例规模与约定】
//   n 最大到 2^31 - 1（正好是 int 的最大值 2147483647），
//   力扣难度：简单。题目要求尽量少调接口，二分每次砍掉一半，调用次数
//   O(log n)。
//
// 思路（二分查找之"找第一个满足条件的位置"：left / right 两个下标 + while）
//   版本序列有个很好的性质：一旦某个版本坏了，它后面全是坏的，也就是
//   "好 好 好 坏 坏 坏"这种形式（可能全是坏的，因为 bad 最小是 1）。于是
//   问题变成：在 [1, n] 里找第一个让 isBadVersion 返回 true 的位置。
//
//   做法：始终把答案圈在闭区间 [left, right] 里：
//     ① 初始化 left = 1、right = n，答案一定在这个范围里；
//     ② 循环条件写 while (left < right)——区间只剩一个数时它就是答案，停止；
//     ③ mid 取中点：mid = left + (right - left) / 2；
//     ④ 如果 isBadVersion(mid) 为 true：mid 本身可能就是答案，所以
//        right = mid（注意不是 mid - 1，mid 不能排除）；
//        如果为 false：mid 和它左边的版本都是好的，答案在右边，
//        left = mid + 1；
//     ⑤ 循环结束时 left == right，返回 left。
//
//   边界和坑：
//     - n 可以到 2^31 - 1，mid 千万别写 (left + right) / 2，left + right 会
//       超出 int 变成负数，导致死循环甚至越界。要写
//       left + (right - left) / 2，先减后加，结果一定在 [left, right] 内，
//       不会溢出；
//     - 判断为 true 时 right = mid 而不是 mid - 1，这和 704 那种"找一个值"
//       不同：那道题比过的 mid 可以直接排除，这道题 mid 正是嫌疑答案；
//     - 循环条件写 left < right 而不是 <=，因为区间只剩一个元素时答案就定
//       了，再进去 mid 会一直等于 left，容易原地打转；
//     - n = 1 时一次循环都不进，直接返回 1，正好覆盖例 2；
//     - 最多调用 isBadVersion O(log n) 次，额外空间 O(1)。
// ============================================================================

public class Solution extends VersionControl {
    public int firstBadVersion(int n) {
        // 边界：n = 1 时一次循环都不进，直接返回 1，正好覆盖例 2
        long left = 1;
        long right = n;
        // 循环条件写 while (left < right)——区间只剩一个数时它就是答案，停止
        while(left < right){
            // mid 取中点：left + (right - left) / 2，先减后加，结果一定在 [left, right] 内，不会溢出
            long mid = left + (right - left) / 2;
            // 如果 isBadVersion(mid) 为 true：mid 本身可能就是答案，所以 right = mid（注意不是 mid - 1，mid 不能排除）
            if(isBadVersion((int)mid) == true){
                right = mid;
                // 如果为 false：mid 和它左边的版本都是好的，答案在右边，left = mid + 1
            }else if(isBadVersion((int)mid) == false){
                left = mid + 1;
            }
        }
        // 循环结束时 left == right，返回 left
        return (int)left;
    }

    private boolean isBadVersion(int mid) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isBadVersion'");
    }
}